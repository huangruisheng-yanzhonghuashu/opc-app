<template>
  <div class="login">
    <!-- 左侧品牌展示区 -->
    <div class="login-brand">
      <div class="brand-content">
        <div class="brand-logo">
          <svg-icon icon-class="dashboard" class="logo-icon" />
        </div>
        <h1 class="brand-title">{{ title }}</h1>
        <p class="brand-subtitle">智能化运维管理平台</p>
        <div class="brand-features">
          <div class="feature-item">
            <div class="feature-icon">
              <svg-icon icon-class="monitor" />
            </div>
            <span>实时监控</span>
          </div>
          <div class="feature-item">
            <div class="feature-icon">
              <svg-icon icon-class="chart" />
            </div>
            <span>数据分析</span>
          </div>
          <div class="feature-item">
            <div class="feature-icon">
              <svg-icon icon-class="server" />
            </div>
            <span>资源管理</span>
          </div>
        </div>
      </div>
      <!-- 装饰性背景元素 -->
      <div class="brand-decoration">
        <div class="deco-circle circle-1"></div>
        <div class="deco-circle circle-2"></div>
        <div class="deco-circle circle-3"></div>
      </div>
    </div>

    <!-- 右侧登录表单区 -->
    <div class="login-form-wrapper">
      <div class="login-form-container">
        <div class="form-header">
          <h2 class="form-title">欢迎回来</h2>
          <p class="form-subtitle">请登录您的账号以继续</p>
        </div>

        <el-form ref="loginRef" :model="loginForm" :rules="loginRules" class="login-form">
          <el-form-item prop="username">
            <div class="input-label">账号</div>
            <el-input
              v-model="loginForm.username"
              type="text"
              size="large"
              auto-complete="off"
              placeholder="请输入账号"
              class="custom-input"
            >
              <template #prefix><svg-icon icon-class="user" class="input-icon" /></template>
            </el-input>
          </el-form-item>

          <el-form-item prop="password">
            <div class="input-label">密码</div>
            <el-input
              v-model="loginForm.password"
              type="password"
              size="large"
              auto-complete="off"
              placeholder="请输入密码"
              class="custom-input"
              @keyup.enter="handleLogin"
            >
              <template #prefix><svg-icon icon-class="password" class="input-icon" /></template>
            </el-input>
          </el-form-item>

          <el-form-item prop="code" v-if="captchaEnabled">
            <div class="input-label">验证码</div>
            <div class="code-input-wrapper">
              <el-input
                v-model="loginForm.code"
                size="large"
                auto-complete="off"
                placeholder="请输入验证码"
                class="custom-input code-input"
                @keyup.enter="handleLogin"
              >
                <template #prefix><svg-icon icon-class="validCode" class="input-icon" /></template>
              </el-input>
              <div class="login-code">
                <img :src="codeUrl" @click="getCode" class="login-code-img"/>
              </div>
            </div>
          </el-form-item>

          <div class="form-options">
            <el-checkbox v-model="loginForm.rememberMe" class="remember-checkbox">记住密码</el-checkbox>
            <a href="#" class="forgot-link" v-if="false">忘记密码？</a>
          </div>

          <el-form-item class="submit-item">
            <el-button
              :loading="loading"
              size="large"
              type="primary"
              class="login-button"
              @click.prevent="handleLogin"
            >
              <span v-if="!loading">登 录</span>
              <span v-else>登 录 中...</span>
            </el-button>
          </el-form-item>

          <div class="register-link" v-if="register">
            <span>还没有账号？</span>
            <router-link class="link-type" :to="'/register'">立即注册</router-link>
          </div>
        </el-form>
      </div>
    </div>

    <!--  底部  -->
    <div class="el-login-footer">
      <span>{{ footerContent }}</span>
    </div>
  </div>
</template>

<script setup>
import { getCodeImg } from "@/api/login"
import Cookies from "js-cookie"
import { encrypt, decrypt } from "@/utils/jsencrypt"
import useUserStore from '@/store/modules/user'
import defaultSettings from '@/settings'

const title = import.meta.env.VITE_APP_TITLE
const footerContent = defaultSettings.footerContent
const userStore = useUserStore()
const route = useRoute()
const router = useRouter()
const { proxy } = getCurrentInstance()

const loginForm = ref({
  username: "admin",
  password: "admin123",
  rememberMe: false,
  code: "",
  uuid: ""
})

const loginRules = {
  username: [{ required: true, trigger: "blur", message: "请输入您的账号" }],
  password: [{ required: true, trigger: "blur", message: "请输入您的密码" }],
  code: [{ required: true, trigger: "change", message: "请输入验证码" }]
}

const codeUrl = ref("")
const loading = ref(false)
// 验证码开关
const captchaEnabled = ref(true)
// 注册开关
const register = ref(false)
const redirect = ref(undefined)

watch(route, (newRoute) => {
    redirect.value = newRoute.query && newRoute.query.redirect
}, { immediate: true })

function handleLogin() {
  proxy.$refs.loginRef.validate(valid => {
    if (valid) {
      loading.value = true
      // 勾选了需要记住密码设置在 cookie 中设置记住用户名和密码
      if (loginForm.value.rememberMe) {
        Cookies.set("username", loginForm.value.username, { expires: 30 })
        Cookies.set("password", encrypt(loginForm.value.password), { expires: 30 })
        Cookies.set("rememberMe", loginForm.value.rememberMe, { expires: 30 })
      } else {
        // 否则移除
        Cookies.remove("username")
        Cookies.remove("password")
        Cookies.remove("rememberMe")
      }
      // 调用action的登录方法
      userStore.login(loginForm.value).then(() => {
        const query = route.query
        const otherQueryParams = Object.keys(query).reduce((acc, cur) => {
          if (cur !== "redirect") {
            acc[cur] = query[cur]
          }
          return acc
        }, {})
        router.push({ path: redirect.value || "/", query: otherQueryParams })
      }).catch(() => {
        loading.value = false
        // 重新获取验证码
        if (captchaEnabled.value) {
          getCode()
        }
      })
    }
  })
}

function getCode() {
  getCodeImg().then(res => {
    captchaEnabled.value = res.captchaEnabled === undefined ? true : res.captchaEnabled
    if (captchaEnabled.value) {
      codeUrl.value = "data:image/gif;base64," + res.img
      loginForm.value.uuid = res.uuid
    }
  })
}

function getCookie() {
  const username = Cookies.get("username")
  const password = Cookies.get("password")
  const rememberMe = Cookies.get("rememberMe")
  loginForm.value = {
    username: username === undefined ? loginForm.value.username : username,
    password: password === undefined ? loginForm.value.password : decrypt(password),
    rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
  }
}

getCode()
getCookie()
</script>

<style lang='scss' scoped>
.login {
  display: flex;
  height: 100vh;
  width: 100vw;
  overflow: hidden;
  position: relative;
}

// 左侧品牌展示区
.login-brand {
  flex: 1;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 50%, #f093fb 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;

  .brand-content {
    text-align: center;
    color: #fff;
    z-index: 2;
    padding: 40px;
  }

  .brand-logo {
    margin-bottom: 30px;

    .logo-icon {
      font-size: 80px;
      color: #fff;
    }
  }

  .brand-title {
    font-size: 48px;
    font-weight: 700;
    margin: 0 0 16px 0;
    letter-spacing: 4px;
    text-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
  }

  .brand-subtitle {
    font-size: 20px;
    font-weight: 300;
    margin: 0 0 50px 0;
    opacity: 0.9;
    letter-spacing: 2px;
  }

  .brand-features {
    display: flex;
    justify-content: center;
    gap: 40px;
    margin-top: 60px;

    .feature-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 12px;

      .feature-icon {
        width: 60px;
        height: 60px;
        background: rgba(255, 255, 255, 0.2);
        border-radius: 16px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 28px;
        backdrop-filter: blur(10px);
        transition: all 0.3s ease;

        &:hover {
          transform: translateY(-5px);
          background: rgba(255, 255, 255, 0.3);
        }
      }

      span {
        font-size: 14px;
        opacity: 0.9;
        letter-spacing: 1px;
      }
    }
  }

  // 装饰性背景元素
  .brand-decoration {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    overflow: hidden;

    .deco-circle {
      position: absolute;
      border-radius: 50%;
      background: rgba(255, 255, 255, 0.05);
    }

    .circle-1 {
      width: 400px;
      height: 400px;
      top: -100px;
      right: -100px;
      animation: float 8s ease-in-out infinite;
    }

    .circle-2 {
      width: 300px;
      height: 300px;
      bottom: 10%;
      left: -80px;
      animation: float 10s ease-in-out infinite reverse;
    }

    .circle-3 {
      width: 200px;
      height: 200px;
      top: 40%;
      right: 10%;
      animation: float 12s ease-in-out infinite;
    }
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0) scale(1);
  }
  50% {
    transform: translateY(-20px) scale(1.05);
  }
}

// 右侧登录表单区
.login-form-wrapper {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e8ec 100%);
  position: relative;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background:
      radial-gradient(circle at 20% 80%, rgba(102, 126, 234, 0.05) 0%, transparent 50%),
      radial-gradient(circle at 80% 20%, rgba(118, 75, 162, 0.05) 0%, transparent 50%);
  }
}

.login-form-container {
  width: 420px;
  padding: 50px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 24px;
  box-shadow:
    0 20px 60px rgba(0, 0, 0, 0.1),
    0 0 0 1px rgba(255, 255, 255, 0.5) inset;
  backdrop-filter: blur(20px);
  z-index: 2;
  animation: slideIn 0.6s ease-out;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.form-header {
  text-align: center;
  margin-bottom: 40px;

  .form-title {
    font-size: 32px;
    font-weight: 700;
    color: #1a1a2e;
    margin: 0 0 10px 0;
  }

  .form-subtitle {
    font-size: 14px;
    color: #888;
    margin: 0;
  }
}

.login-form {
  .input-label {
    font-size: 14px;
    font-weight: 500;
    color: #555;
    margin-bottom: 8px;
  }

  .custom-input {
    :deep(.el-input__wrapper) {
      background: #f8f9fa;
      border-radius: 12px;
      box-shadow: none;
      border: 2px solid transparent;
      transition: all 0.3s ease;
      padding: 0 15px;

      &:hover, &:focus-within {
        background: #fff;
        border-color: #667eea;
        box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.1);
      }
    }

    :deep(.el-input__inner) {
      height: 50px;
      font-size: 15px;
      color: #333;

      &::placeholder {
        color: #aaa;
      }
    }

    .input-icon {
      font-size: 18px;
      color: #999;
      margin-right: 8px;
    }
  }

  .code-input-wrapper {
    display: flex;
    gap: 12px;

    .code-input {
      flex: 1;
    }

    .login-code {
      width: 120px;
      height: 50px;
      border-radius: 12px;
      overflow: hidden;
      cursor: pointer;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
      transition: transform 0.2s ease;

      &:hover {
        transform: scale(1.02);
      }

      .login-code-img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
    }
  }

  .form-options {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin: 20px 0 30px 0;

    .remember-checkbox {
      :deep(.el-checkbox__label) {
        color: #666;
        font-size: 13px;
      }

      :deep(.el-checkbox__input.is-checked .el-checkbox__inner) {
        background-color: #667eea;
        border-color: #667eea;
      }
    }

    .forgot-link {
      color: #667eea;
      font-size: 13px;
      text-decoration: none;
      transition: color 0.3s ease;

      &:hover {
        color: #764ba2;
      }
    }
  }

  .submit-item {
    margin-bottom: 0;
  }

  .login-button {
    width: 100%;
    height: 52px;
    font-size: 16px;
    font-weight: 600;
    letter-spacing: 2px;
    border-radius: 12px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border: none;
    box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 12px 30px rgba(102, 126, 234, 0.5);
    }

    &:active {
      transform: translateY(0);
    }
  }

  .register-link {
    text-align: center;
    margin-top: 24px;
    font-size: 14px;
    color: #666;

    .link-type {
      color: #667eea;
      text-decoration: none;
      font-weight: 500;
      margin-left: 4px;
      transition: color 0.3s ease;

      &:hover {
        color: #764ba2;
      }
    }
  }
}

// 底部版权
.el-login-footer {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 50px;
  line-height: 50px;
  text-align: center;
  color: rgba(255, 255, 255, 0.8);
  font-size: 12px;
  letter-spacing: 1px;
  z-index: 10;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.3), transparent);
}

// 响应式设计
@media screen and (max-width: 1024px) {
  .login-brand {
    display: none;
  }

  .login-form-wrapper {
    flex: 1;
    padding: 20px;
  }

  .login-form-container {
    width: 100%;
    max-width: 420px;
    padding: 40px 30px;
  }
}

@media screen and (max-width: 480px) {
  .login-form-container {
    padding: 30px 20px;
    border-radius: 20px;
  }

  .form-header {
    .form-title {
      font-size: 26px;
    }
  }
}
</style>
