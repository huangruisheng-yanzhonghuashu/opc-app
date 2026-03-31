package com.opc.web.service.twitter.v2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opc.common.config.SopConfig;
import com.opc.core.domain.CoreMaterial;
import com.opc.core.service.ICoreMaterialService;
import com.opc.web.config.twitter.v2.TwitterApiV2Properties;
import com.opc.web.dto.twitter.v2.TweetDTO;
import com.opc.web.dto.twitter.v2.TwitterSearchRequestDTO;
import com.opc.web.dto.twitter.v2.TwitterSearchResponseDTO;
import kong.unirest.Config;
import kong.unirest.HttpResponse;
import kong.unirest.Proxy;
import kong.unirest.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * Twitter API v2 服务实现类
 * <p>
 * 使用 Unirest 实现 Twitter API v2 相关操作，包括搜索推文等功能
 * </p>
 *
 * @author opc
 * @since 3.9.1
 */
@Service
public class TwitterApiV2ServiceImpl implements TwitterApiV2Service {

    private static final Logger log = LoggerFactory.getLogger(TwitterApiV2ServiceImpl.class);

    private static final String SEARCH_RECENT_ENDPOINT = "/tweets/search/recent";

    @Autowired
    private TwitterApiV2Properties twitterApiV2Properties;

    @Autowired
    private ICoreMaterialService materialService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        // 配置 Unirest
        Config config = Unirest.config()
                .connectTimeout(twitterApiV2Properties.getTimeout())
                .socketTimeout(twitterApiV2Properties.getTimeout())
                .concurrency(100, 20);

        // 设置代理
        if (twitterApiV2Properties.isProxyEnabled()) {
            Proxy proxy = new Proxy(twitterApiV2Properties.getProxyHost(), twitterApiV2Properties.getProxyPort());
            config.proxy(proxy);
            log.info("Unirest 代理已启用: {}:{}",
                    twitterApiV2Properties.getProxyHost(),
                    twitterApiV2Properties.getProxyPort());
        }

        log.info("Unirest 初始化完成");
    }

    @PreDestroy
    public void shutdown() {
        Unirest.shutDown();
        log.info("Unirest 已关闭");
    }

    @Override
    public TwitterSearchResponseDTO searchRecentTweets(TwitterSearchRequestDTO request){
        //
/*        String body = "{\n" +
                "  \"data\" : [ {\n" +
                "    \"edit_history_tweet_ids\" : [ \"2038546535810564481\" ],\n" +
                "    \"id\" : \"2038546535810564481\",\n" +
                "    \"text\" : \"我集合了很多美国卡bin  冷门，干净 。事实上 只需要稳定给部分opc和个人开发者用  不污染卡池  大家就都好 https://t.co/ymdnhDQLaE\",\n" +
                "    \"author_id\" : \"1910008069586862080\",\n" +
                "    \"created_at\" : \"2026-03-30T09:19:03.000Z\",\n" +
                "    \"entities\" : {\n" +
                "      \"urls\" : [ {\n" +
                "        \"start\" : 58,\n" +
                "        \"end\" : 81,\n" +
                "        \"url\" : \"https://t.co/ymdnhDQLaE\",\n" +
                "        \"expanded_url\" : \"https://x.com/Zechen_Ethan/status/2038546535810564481/photo/1\",\n" +
                "        \"display_url\" : \"pic.x.com/ymdnhDQLaE\",\n" +
                "        \"media_key\" : \"3_2038546196399378432\"\n" +
                "      }, {\n" +
                "        \"start\" : 58,\n" +
                "        \"end\" : 81,\n" +
                "        \"url\" : \"https://t.co/ymdnhDQLaE\",\n" +
                "        \"expanded_url\" : \"https://x.com/Zechen_Ethan/status/2038546535810564481/photo/1\",\n" +
                "        \"display_url\" : \"pic.x.com/ymdnhDQLaE\",\n" +
                "        \"media_key\" : \"3_2038546196289953792\"\n" +
                "      } ]\n" +
                "    },\n" +
                "    \"attachments\" : {\n" +
                "      \"media_keys\" : [ \"3_2038546196399378432\", \"3_2038546196289953792\" ]\n" +
                "    },\n" +
                "    \"lang\" : \"zh\",\n" +
                "    \"public_metrics\" : {\n" +
                "      \"retweet_count\" : 0,\n" +
                "      \"reply_count\" : 0,\n" +
                "      \"like_count\" : 0,\n" +
                "      \"quote_count\" : 0,\n" +
                "      \"bookmark_count\" : 0,\n" +
                "      \"impression_count\" : 2\n" +
                "    }\n" +
                "  }, {\n" +
                "    \"edit_history_tweet_ids\" : [ \"2038541462405566539\" ],\n" +
                "    \"id\" : \"2038541462405566539\",\n" +
                "    \"text\" : \"@ParallelFacts What did you expect from a Pan Yoruba who sponsored OPC to massacre the Hausa/Fulani community during his time as Lagos Governor.\",\n" +
                "    \"author_id\" : \"1569308384544833537\",\n" +
                "    \"created_at\" : \"2026-03-30T08:58:53.000Z\",\n" +
                "    \"entities\" : {\n" +
                "      \"mentions\" : [ {\n" +
                "        \"start\" : 0,\n" +
                "        \"end\" : 14,\n" +
                "        \"username\" : \"ParallelFacts\",\n" +
                "        \"id\" : \"1653894267159314433\"\n" +
                "      } ]\n" +
                "    },\n" +
                "    \"lang\" : \"en\",\n" +
                "    \"public_metrics\" : {\n" +
                "      \"retweet_count\" : 0,\n" +
                "      \"reply_count\" : 0,\n" +
                "      \"like_count\" : 0,\n" +
                "      \"quote_count\" : 0,\n" +
                "      \"bookmark_count\" : 0,\n" +
                "      \"impression_count\" : 6\n" +
                "    }\n" +
                "  }, {\n" +
                "    \"edit_history_tweet_ids\" : [ \"2038540481265606776\" ],\n" +
                "    \"id\" : \"2038540481265606776\",\n" +
                "    \"text\" : \"The OPC era has arrived\\n let's take a look at how Fengwo has achieved it https://t.co/k6xa1LeFpL\",\n" +
                "    \"author_id\" : \"2009519045910122498\",\n" +
                "    \"created_at\" : \"2026-03-30T08:54:59.000Z\",\n" +
                "    \"entities\" : {\n" +
                "      \"urls\" : [ {\n" +
                "        \"start\" : 73,\n" +
                "        \"end\" : 96,\n" +
                "        \"url\" : \"https://t.co/k6xa1LeFpL\",\n" +
                "        \"expanded_url\" : \"https://x.com/EiAmine71981/status/2038540481265606776/photo/1\",\n" +
                "        \"display_url\" : \"pic.x.com/k6xa1LeFpL\",\n" +
                "        \"media_key\" : \"3_2038540478363144192\"\n" +
                "      } ]\n" +
                "    },\n" +
                "    \"attachments\" : {\n" +
                "      \"media_keys\" : [ \"3_2038540478363144192\" ]\n" +
                "    },\n" +
                "    \"lang\" : \"en\",\n" +
                "    \"public_metrics\" : {\n" +
                "      \"retweet_count\" : 0,\n" +
                "      \"reply_count\" : 0,\n" +
                "      \"like_count\" : 0,\n" +
                "      \"quote_count\" : 0,\n" +
                "      \"bookmark_count\" : 0,\n" +
                "      \"impression_count\" : 3\n" +
                "    }\n" +
                "  }, {\n" +
                "    \"edit_history_tweet_ids\" : [ \"2038539757597172109\" ],\n" +
                "    \"id\" : \"2038539757597172109\",\n" +
                "    \"text\" : \"The fight sprawled to all locations the two groups are located. \\nEven they fought too in Cameroon before they were calmed. \\n\\nThey fought in Lagos too.... Even before OPC take charge for Ajegunle, we fought at JMJ, Itun àgàn, Igbo elejo (Snake Island)... The fight leads to erasure https://t.co/DHfP028YoY\",\n" +
                "    \"author_id\" : \"1481694597126541312\",\n" +
                "    \"created_at\" : \"2026-03-30T08:52:07.000Z\",\n" +
                "    \"entities\" : {\n" +
                "      \"urls\" : [ {\n" +
                "        \"start\" : 281,\n" +
                "        \"end\" : 304,\n" +
                "        \"url\" : \"https://t.co/DHfP028YoY\",\n" +
                "        \"expanded_url\" : \"https://twitter.com/dvearse/status/2038538507082186864\",\n" +
                "        \"display_url\" : \"x.com/dvearse/status…\"\n" +
                "      } ]\n" +
                "    },\n" +
                "    \"lang\" : \"en\",\n" +
                "    \"public_metrics\" : {\n" +
                "      \"retweet_count\" : 0,\n" +
                "      \"reply_count\" : 0,\n" +
                "      \"like_count\" : 0,\n" +
                "      \"quote_count\" : 0,\n" +
                "      \"bookmark_count\" : 0,\n" +
                "      \"impression_count\" : 20\n" +
                "    }\n" +
                "  }, {\n" +
                "    \"edit_history_tweet_ids\" : [ \"2038539729420116177\" ],\n" +
                "    \"id\" : \"2038539729420116177\",\n" +
                "    \"text\" : \"opc最大的问题是需要社交。\\n所以，才会有opc社区这样的产物存在。\",\n" +
                "    \"author_id\" : \"1969297652698529793\",\n" +
                "    \"created_at\" : \"2026-03-30T08:52:00.000Z\",\n" +
                "    \"lang\" : \"zh\",\n" +
                "    \"public_metrics\" : {\n" +
                "      \"retweet_count\" : 0,\n" +
                "      \"reply_count\" : 0,\n" +
                "      \"like_count\" : 0,\n" +
                "      \"quote_count\" : 0,\n" +
                "      \"bookmark_count\" : 0,\n" +
                "      \"impression_count\" : 2\n" +
                "    }\n" +
                "  }, {\n" +
                "    \"edit_history_tweet_ids\" : [ \"2038538507082186864\" ],\n" +
                "    \"id\" : \"2038538507082186864\",\n" +
                "    \"text\" : \"@LOSEYIE @akintollgate Oooh. That was the period OPC was fighting them in Ajegunle, too. I guess. Do OPC help at all\",\n" +
                "    \"author_id\" : \"755011214439251968\",\n" +
                "    \"created_at\" : \"2026-03-30T08:47:08.000Z\",\n" +
                "    \"entities\" : {\n" +
                "      \"mentions\" : [ {\n" +
                "        \"start\" : 0,\n" +
                "        \"end\" : 8,\n" +
                "        \"username\" : \"LOSEYIE\",\n" +
                "        \"id\" : \"1481694597126541312\"\n" +
                "      }, {\n" +
                "        \"start\" : 9,\n" +
                "        \"end\" : 22,\n" +
                "        \"username\" : \"akintollgate\",\n" +
                "        \"id\" : \"1325419982004637698\"\n" +
                "      } ]\n" +
                "    },\n" +
                "    \"lang\" : \"en\",\n" +
                "    \"public_metrics\" : {\n" +
                "      \"retweet_count\" : 0,\n" +
                "      \"reply_count\" : 0,\n" +
                "      \"like_count\" : 1,\n" +
                "      \"quote_count\" : 1,\n" +
                "      \"bookmark_count\" : 0,\n" +
                "      \"impression_count\" : 26\n" +
                "    }\n" +
                "  }, {\n" +
                "    \"edit_history_tweet_ids\" : [ \"2038526894698860595\" ],\n" +
                "    \"id\" : \"2038526894698860595\",\n" +
                "    \"text\" : \"\uD83D\uDCF0 La presidenta de OPC España, Matilde Almandoz Ríos, protagoniza la Tribuna de la Revista Congresos de @puntomice.\\n\\nEn una edición dedicada a la etapa poscongreso, Matilde enumera las cualidades de las OPC y la urgencia de profesionalizar completamente el sector:\\n\uD83D\uDC49\uD83C\uDFFB Destaca en https://t.co/CFW5WY4ulP\",\n" +
                "    \"author_id\" : \"864704310\",\n" +
                "    \"created_at\" : \"2026-03-30T08:01:00.000Z\",\n" +
                "    \"entities\" : {\n" +
                "      \"mentions\" : [ {\n" +
                "        \"start\" : 104,\n" +
                "        \"end\" : 114,\n" +
                "        \"username\" : \"puntomice\",\n" +
                "        \"id\" : \"2417264383\"\n" +
                "      } ],\n" +
                "      \"urls\" : [ {\n" +
                "        \"start\" : 279,\n" +
                "        \"end\" : 302,\n" +
                "        \"url\" : \"https://t.co/CFW5WY4ulP\",\n" +
                "        \"expanded_url\" : \"https://x.com/opcspain/status/2038526894698860595/photo/1\",\n" +
                "        \"display_url\" : \"pic.x.com/CFW5WY4ulP\",\n" +
                "        \"media_key\" : \"3_2037544840993632256\"\n" +
                "      } ]\n" +
                "    },\n" +
                "    \"attachments\" : {\n" +
                "      \"media_keys\" : [ \"3_2037544840993632256\" ]\n" +
                "    },\n" +
                "    \"lang\" : \"es\",\n" +
                "    \"public_metrics\" : {\n" +
                "      \"retweet_count\" : 0,\n" +
                "      \"reply_count\" : 0,\n" +
                "      \"like_count\" : 1,\n" +
                "      \"quote_count\" : 0,\n" +
                "      \"bookmark_count\" : 0,\n" +
                "      \"impression_count\" : 7\n" +
                "    }\n" +
                "  }, {\n" +
                "    \"edit_history_tweet_ids\" : [ \"2038525425022799936\" ],\n" +
                "    \"id\" : \"2038525425022799936\",\n" +
                "    \"text\" : \"@kayodebakre8 @Juwon_miles And you think the war will be fought like Opc and Nigeria police, keep dreaming when they tell you the USA Empire is falling you think is a joke. The Iran war has proved it\",\n" +
                "    \"author_id\" : \"1627737072617328664\",\n" +
                "    \"created_at\" : \"2026-03-30T07:55:09.000Z\",\n" +
                "    \"entities\" : {\n" +
                "      \"mentions\" : [ {\n" +
                "        \"start\" : 0,\n" +
                "        \"end\" : 13,\n" +
                "        \"username\" : \"kayodebakre8\",\n" +
                "        \"id\" : \"972122845\"\n" +
                "      }, {\n" +
                "        \"start\" : 14,\n" +
                "        \"end\" : 26,\n" +
                "        \"username\" : \"Juwon_miles\",\n" +
                "        \"id\" : \"1376511813056094212\"\n" +
                "      } ]\n" +
                "    },\n" +
                "    \"lang\" : \"en\",\n" +
                "    \"public_metrics\" : {\n" +
                "      \"retweet_count\" : 0,\n" +
                "      \"reply_count\" : 0,\n" +
                "      \"like_count\" : 0,\n" +
                "      \"quote_count\" : 0,\n" +
                "      \"bookmark_count\" : 0,\n" +
                "      \"impression_count\" : 61\n" +
                "    }\n" +
                "  }, {\n" +
                "    \"edit_history_tweet_ids\" : [ \"2038515177306427484\" ],\n" +
                "    \"id\" : \"2038515177306427484\",\n" +
                "    \"text\" : \"@Bayo_Bilisi Our OPC ?  Haa egbon mi\",\n" +
                "    \"author_id\" : \"1940853490177150977\",\n" +
                "    \"created_at\" : \"2026-03-30T07:14:26.000Z\",\n" +
                "    \"entities\" : {\n" +
                "      \"mentions\" : [ {\n" +
                "        \"start\" : 0,\n" +
                "        \"end\" : 12,\n" +
                "        \"username\" : \"Bayo_Bilisi\",\n" +
                "        \"id\" : \"1884638317787873281\"\n" +
                "      } ]\n" +
                "    },\n" +
                "    \"lang\" : \"ht\",\n" +
                "    \"public_metrics\" : {\n" +
                "      \"retweet_count\" : 0,\n" +
                "      \"reply_count\" : 0,\n" +
                "      \"like_count\" : 0,\n" +
                "      \"quote_count\" : 0,\n" +
                "      \"bookmark_count\" : 0,\n" +
                "      \"impression_count\" : 20\n" +
                "    }\n" +
                "  }, {\n" +
                "    \"edit_history_tweet_ids\" : [ \"2038508492865323024\" ],\n" +
                "    \"id\" : \"2038508492865323024\",\n" +
                "    \"text\" : \"#ありがとうTEPPEN\\n\\nTEPPENのコミュニティは優しくてプレイヤーとも呼べないただのおっちゃんユーザーでも様々な人が相手をしてくれた\\n誘ってもらったクランOPCは緩くて居心地がよかった\\n\\n何度も何度も言ったけどTEPPENプレイヤーの民度は高かった\\n居心地がよかった\\nありがとう\\n全てのTEPPENプレイヤー\",\n" +
                "    \"author_id\" : \"2806769214\",\n" +
                "    \"created_at\" : \"2026-03-30T06:47:53.000Z\",\n" +
                "    \"entities\" : {\n" +
                "      \"hashtags\" : [ {\n" +
                "        \"start\" : 0,\n" +
                "        \"end\" : 12,\n" +
                "        \"tag\" : \"ありがとうTEPPEN\"\n" +
                "      } ]\n" +
                "    },\n" +
                "    \"lang\" : \"ja\",\n" +
                "    \"public_metrics\" : {\n" +
                "      \"retweet_count\" : 2,\n" +
                "      \"reply_count\" : 1,\n" +
                "      \"like_count\" : 16,\n" +
                "      \"quote_count\" : 0,\n" +
                "      \"bookmark_count\" : 0,\n" +
                "      \"impression_count\" : 182\n" +
                "    }\n" +
                "  } ],\n" +
                "  \"includes\" : {\n" +
                "    \"media\" : [ {\n" +
                "      \"url\" : \"https://pbs.twimg.com/media/HEpe9wab0AABrit.jpg\",\n" +
                "      \"height\" : 1280,\n" +
                "      \"type\" : \"photo\",\n" +
                "      \"media_key\" : \"3_2038546196399378432\",\n" +
                "      \"width\" : 1280\n" +
                "    }, {\n" +
                "      \"url\" : \"https://pbs.twimg.com/media/HEpe9wAWIAAt72o.png\",\n" +
                "      \"height\" : 702,\n" +
                "      \"type\" : \"photo\",\n" +
                "      \"media_key\" : \"3_2038546196289953792\",\n" +
                "      \"width\" : 798\n" +
                "    }, {\n" +
                "      \"url\" : \"https://pbs.twimg.com/media/HEpZw7EXEAA_nPT.jpg\",\n" +
                "      \"height\" : 2000,\n" +
                "      \"type\" : \"photo\",\n" +
                "      \"media_key\" : \"3_2038540478363144192\",\n" +
                "      \"width\" : 800\n" +
                "    }, {\n" +
                "      \"url\" : \"https://pbs.twimg.com/media/HEbQPM1a8AA92YD.jpg\",\n" +
                "      \"height\" : 2701,\n" +
                "      \"type\" : \"photo\",\n" +
                "      \"media_key\" : \"3_2037544840993632256\",\n" +
                "      \"width\" : 2160\n" +
                "    } ],\n" +
                "    \"users\" : [ {\n" +
                "      \"id\" : \"1910008069586862080\",\n" +
                "      \"name\" : \"Ethan.大质\",\n" +
                "      \"username\" : \"Zechen_Ethan\"\n" +
                "    }, {\n" +
                "      \"id\" : \"1569308384544833537\",\n" +
                "      \"name\" : \"Big BALLS\",\n" +
                "      \"username\" : \"Amaogudu\"\n" +
                "    }, {\n" +
                "      \"id\" : \"2009519045910122498\",\n" +
                "      \"name\" : \"FENGWO\",\n" +
                "      \"username\" : \"EiAmine71981\"\n" +
                "    }, {\n" +
                "      \"id\" : \"1481694597126541312\",\n" +
                "      \"name\" : \"EMPEROR LỌ́GỌ̀\",\n" +
                "      \"username\" : \"LOSEYIE\"\n" +
                "    }, {\n" +
                "      \"id\" : \"1969297652698529793\",\n" +
                "      \"name\" : \"k lin\",\n" +
                "      \"username\" : \"klin279832\"\n" +
                "    }, {\n" +
                "      \"id\" : \"755011214439251968\",\n" +
                "      \"name\" : \"D Vearse\",\n" +
                "      \"username\" : \"dvearse\"\n" +
                "    }, {\n" +
                "      \"id\" : \"864704310\",\n" +
                "      \"name\" : \"OPC España\",\n" +
                "      \"username\" : \"opcspain\"\n" +
                "    }, {\n" +
                "      \"id\" : \"1627737072617328664\",\n" +
                "      \"name\" : \"Icenberg \uD83E\uDDCA\",\n" +
                "      \"username\" : \"simplykaycee411\"\n" +
                "    }, {\n" +
                "      \"id\" : \"1940853490177150977\",\n" +
                "      \"name\" : \"MFAFO\",\n" +
                "      \"username\" : \"nelhoug\"\n" +
                "    }, {\n" +
                "      \"id\" : \"2806769214\",\n" +
                "      \"name\" : \"ぼぶ(E)@OPCω\",\n" +
                "      \"username\" : \"ponkotsuban\"\n" +
                "    } ]\n" +
                "  },\n" +
                "  \"meta\" : {\n" +
                "    \"newest_id\" : \"2038546535810564481\",\n" +
                "    \"oldest_id\" : \"2038508492865323024\",\n" +
                "    \"result_count\" : 10,\n" +
                "    \"next_token\" : \"b26v89c19zqg8o3juehk5kvivays4tefyvs04mdfi5k71\"\n" +
                "  }\n" +
                "}";*/

        try {
            // 校验并修正 maxResults 参数（Twitter API 要求 10-100）
            if (request.getMaxResults() == null || request.getMaxResults() < 10) {
                log.warn("maxResults 参数值 [{}] 小于 Twitter API 最小值 10，已修正为 10", request.getMaxResults());
                request.setMaxResults(10);
            } else if (request.getMaxResults() > 100) {
                log.warn("maxResults 参数值 [{}] 大于 Twitter API 最大值 100，已修正为 100", request.getMaxResults());
                request.setMaxResults(100);
            }

            // 构建基础 URL
            String url = twitterApiV2Properties.getBaseUrl() + SEARCH_RECENT_ENDPOINT;
            log.info("调用 Twitter API v2 搜索接口: {}", url);

            // 构建请求 - 使用 queryString 方式构建参数
            kong.unirest.GetRequest getRequest = Unirest.get(url)
                    .queryString("query", request.getQuery())
                    .queryString("max_results", request.getMaxResults())
                    .queryString("sort_order", "relevancy")
                    .queryString("tweet.fields", "created_at,author_id,public_metrics,text,attachments,entities,lang")
                    .queryString("expansions", "attachments.media_keys,author_id")
                    .queryString("media.fields", "media_key,type,url,preview_image_url,alt_text,duration_ms,width,height")
                    .header("Authorization", "Bearer " + twitterApiV2Properties.getBearerToken());

            // 添加可选参数
            if (request.getNextToken() != null && !request.getNextToken().isEmpty()) {
                getRequest.queryString("next_token", request.getNextToken());
            }
            if (request.getStartTime() != null && !request.getStartTime().isEmpty()) {
                getRequest.queryString("start_time", request.getStartTime());
            }
            if (request.getEndTime() != null && !request.getEndTime().isEmpty()) {
                getRequest.queryString("end_time", request.getEndTime());
            }

            HttpResponse<String> response = getRequest.asString();

            if (response.getStatus() == 200) {
                TwitterSearchResponseDTO result = objectMapper.readValue(
                        response.getBody(),
                        TwitterSearchResponseDTO.class
                );
                log.info("Twitter API 搜索成功，获取 {} 条推文",
                        result.getMeta() != null ? result.getMeta().getResultCount() : 0);

                // 提取推文信息并转换为素材
                List<TweetInfo> tweetInfoList = extractTweetInfo(result);

                // 打印每个推文的媒体类型和URL，并下载媒体文件
                for (TweetInfo info : tweetInfoList) {
                    log.info("推文ID: {}", info.getId());
                    log.info("推文URL: {}", info.getUrl());

                    // 获取媒体列表
                    List<TwitterSearchResponseDTO.Media> mediaList = info.getMediaList();

                    // 初始化URL映射
                    Map<String, String> urlMapping = new HashMap<>();

                    // 遍历媒体并下载
                    for (TwitterSearchResponseDTO.Media media : mediaList) {
                        log.info("  媒体类型: {}", media.getType());        // photo / video / animated_gif
                        log.info("  媒体URL: {}", media.getUrl());           // 图片直接URL
                        log.info("  预览图: {}", media.getPreviewImageUrl()); // 视频封面
                        log.info("  宽度: {}", media.getWidth());
                        log.info("  高度: {}", media.getHeight());

                        // 下载媒体文件
                        String mediaUrl = media.getUrl() != null ? media.getUrl() : media.getPreviewImageUrl();
                        if (mediaUrl != null) {
                            String fileName = generateFileName(media);
                            String localPath = downloadMedia(mediaUrl, fileName);
                            if (localPath != null) {
                                urlMapping.put(mediaUrl, localPath);
                                log.info("  已保存到: {}", localPath);
                            }
                        }
                    }

                    // 设置URL映射到TweetInfo
                    info.setMediaUrlToLocalPath(urlMapping);
                    log.info("---");
                }

                List<CoreMaterial> materials = convertToMaterials(tweetInfoList);

                // 保存到数据库
                int savedCount = 0;
                for (CoreMaterial material : materials) {
                    try {
                        // 检查是否已存在（根据 originalId 去重）
                        CoreMaterial existMaterial = materialService.selectMaterialByOriginalId(material.getOriginalId());
                        if (existMaterial == null) {
                            materialService.insertMaterial(material);
                            savedCount++;
                            log.info("素材已保存到数据库，originalId: {}", material.getOriginalId());
                        } else {
                            log.info("素材已存在，跳过保存，originalId: {}", material.getOriginalId());
                        }
                    } catch (Exception e) {
                        log.error("保存素材到数据库失败，originalId: {}", material.getOriginalId(), e);
                    }
                }
                log.info("共保存 {} 条素材到数据库", savedCount);

                return result;
            } else {
                log.error("Twitter API 返回非成功状态码: {}，响应: {}", response.getStatus(), response.getBody());
                throw new RuntimeException("Twitter API 返回非成功状态码: " + response.getStatus());
            }

        } catch (IOException e) {
            log.error("解析 Twitter API 响应失败", e);
            throw new RuntimeException("解析 Twitter API 响应失败: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("调用 Twitter API v2 搜索接口失败", e);
            throw new RuntimeException("调用 Twitter API v2 搜索接口失败: " + e.getMessage(), e);
        }
    }

    @Override
    public String buildTweetUrl(String tweetId, String authorUsername) {
        if (authorUsername != null && !authorUsername.isEmpty()) {
            return String.format("https://x.com/%s/status/%s", authorUsername, tweetId);
        }
        return String.format("https://x.com/i/status/%s", tweetId);
    }

    /**
     * 从响应中获取推文 URL 和对应的媒体信息
     *
     * @param response Twitter API 响应
     * @return 推文信息列表，包含 URL 和媒体
     */
    public List<TweetInfo> extractTweetInfo(TwitterSearchResponseDTO response) {
        List<TweetInfo> result = new ArrayList<>();

        if (response == null || response.getData() == null || response.getData().isEmpty()) {
            return result;
        }

        // 构建用户ID到用户名的映射
        Map<String, String> userIdToUsername = new HashMap<>();
        if (response.getIncludes() != null && response.getIncludes().getUsers() != null) {
            for (TwitterSearchResponseDTO.User user : response.getIncludes().getUsers()) {
                userIdToUsername.put(user.getId(), user.getUsername());
            }
        }

        // 构建媒体密钥到媒体信息的映射
        Map<String, TwitterSearchResponseDTO.Media> mediaKeyToMedia = new HashMap<>();
        if (response.getIncludes() != null && response.getIncludes().getMedia() != null) {
            for (TwitterSearchResponseDTO.Media media : response.getIncludes().getMedia()) {
                mediaKeyToMedia.put(media.getMediaKey(), media);
            }
        }

        // 遍历推文数据
        for (TweetDTO tweet : response.getData()) {
            TweetInfo info = new TweetInfo();
            info.setId(tweet.getId());
            info.setText(tweet.getText());
            info.setCreatedAt(tweet.getCreatedAt());

            // 构建推文 URL
            String authorUsername = userIdToUsername.get(tweet.getAuthorId());
            info.setUrl(buildTweetUrl(tweet.getId(), authorUsername));
            info.setAuthorUsername(authorUsername);

            // 获取媒体列表
            List<TwitterSearchResponseDTO.Media> mediaList = new ArrayList<>();
            if (tweet.getAttachments() != null && tweet.getAttachments().getMediaKeys() != null) {
                for (String mediaKey : tweet.getAttachments().getMediaKeys()) {
                    TwitterSearchResponseDTO.Media media = mediaKeyToMedia.get(mediaKey);
                    if (media != null) {
                        mediaList.add(media);
                    }
                }
            }
            info.setMediaList(mediaList);

            result.add(info);
        }

        return result;
    }

    /**
     * 推文信息封装类
     */
    public static class TweetInfo {
        private String id;
        private String text;
        private String url;
        private String createdAt;
        private String authorUsername;
        private List<TwitterSearchResponseDTO.Media> mediaList;
        /** 媒体URL到本地路径的映射 */
        private Map<String, String> mediaUrlToLocalPath;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getAuthorUsername() {
            return authorUsername;
        }

        public void setAuthorUsername(String authorUsername) {
            this.authorUsername = authorUsername;
        }

        public List<TwitterSearchResponseDTO.Media> getMediaList() {
            return mediaList;
        }

        public void setMediaList(List<TwitterSearchResponseDTO.Media> mediaList) {
            this.mediaList = mediaList;
        }

        public Map<String, String> getMediaUrlToLocalPath() {
            return mediaUrlToLocalPath;
        }

        public void setMediaUrlToLocalPath(Map<String, String> mediaUrlToLocalPath) {
            this.mediaUrlToLocalPath = mediaUrlToLocalPath;
        }

        /**
         * 获取媒体 URL 列表（只包含图片/视频的直接 URL）
         */
        public List<String> getMediaUrls() {
            if (mediaList == null) {
                return new ArrayList<>();
            }
            return mediaList.stream()
                    .map(media -> media.getUrl() != null ? media.getUrl() : media.getPreviewImageUrl())
                    .filter(url -> url != null)
                    .collect(Collectors.toList());
        }

        @Override
        public String toString() {
            return "TweetInfo{" +
                    "id='" + id + '\'' +
                    ", url='" + url + '\'' +
                    ", authorUsername='" + authorUsername + '\'' +
                    ", mediaCount=" + (mediaList != null ? mediaList.size() : 0) +
                    '}';
        }
    }

    /**
     * 将推文信息列表转换为素材列表
     *
     * @param tweetInfoList 推文信息列表
     * @return 素材列表
     */
    public List<CoreMaterial> convertToMaterials(List<TweetInfo> tweetInfoList) {
        List<CoreMaterial> materials = new ArrayList<>();

        for (TweetInfo info : tweetInfoList) {
            CoreMaterial material = new CoreMaterial();

            // 设置原ID
            material.setOriginalId(info.getId());

            // 设置内容 - 移除推文中的 t.co URL，在末尾添加图片/视频HTML
            String content = info.getText();
            if (content != null) {
                // 移除 t.co 短链接（通常是推文末尾的媒体链接）
                content = content.replaceAll("\\s*https?://t\\.co/\\w+\\s*$", "");

                // 如果有媒体文件，在末尾添加 HTML 标签
                if (info.getMediaUrlToLocalPath() != null && !info.getMediaUrlToLocalPath().isEmpty()) {
                    StringBuilder mediaHtml = new StringBuilder();
                    for (Map.Entry<String, String> entry : info.getMediaUrlToLocalPath().entrySet()) {
                        String localPath = entry.getValue();
                        // 获取媒体信息
                        TwitterSearchResponseDTO.Media media = getMediaByUrl(entry.getKey(), info.getMediaList());
                        String mediaType = media != null ? media.getType() : "photo";

                        if ("video".equals(mediaType) || "animated_gif".equals(mediaType)) {
                            // 视频使用 video 标签
                            mediaHtml.append("<video src=\"").append(localPath).append("\" controls style=\"max-width:100%;\"></video><br>");
                        } else {
                            // 图片使用 img 标签，添加宽高属性
                            Integer width = media != null ? media.getWidth() : null;
                            Integer height = media != null ? media.getHeight() : null;
                            mediaHtml.append("<img src=\"").append(localPath).append("\"");
                            if (width != null) {
                                mediaHtml.append(" width=\"").append(width).append("\"");
                            }
                            if (height != null) {
                                mediaHtml.append(" height=\"").append(height).append("\"");
                            }
                            mediaHtml.append(" style=\"max-width:100%;\" /><br>");
                        }
                    }
                    content = content + "<br>" + mediaHtml.toString();
                }
            }
            material.setContent(content);

            // 设置标题（取前50字符）
            String title = info.getText() != null && !info.getText().isEmpty()
                    ? (info.getText().length() > 50 ? info.getText().substring(0, 50) + "..." : info.getText())
                    : "Twitter 内容";
            material.setTitle(title);

            // 设置作者
            material.setAuthor(info.getAuthorUsername());

            // 设置原链接
            material.setOriginalUrl(info.getUrl());

            // 设置来源为 Twitter
            material.setSource("twitter");

            // 设置内容类型
            if (info.getMediaList() != null && !info.getMediaList().isEmpty()) {
                String mediaType = info.getMediaList().get(0).getType();
                // 获取第一张媒体的本地路径
                String firstMediaUrl = info.getMediaUrls().get(0);
                String localPath = info.getMediaUrlToLocalPath() != null
                        ? info.getMediaUrlToLocalPath().get(firstMediaUrl)
                        : null;

                if ("video".equals(mediaType) || "animated_gif".equals(mediaType)) {
                    material.setContentType("video");
                    material.setVideoUrl(localPath != null ? localPath : firstMediaUrl);
                } else {
                    material.setContentType("image");
                }
            } else {
                material.setContentType("text");
            }

            // 设置默认状态为下线
            material.setStatus("1");

            // 设置默认套餐类型为普通会员
            material.setPackageType(1);

            materials.add(material);
        }

        return materials;
    }

    /**
     * 根据原始URL获取媒体类型
     */
    private String getMediaTypeByUrl(String url, List<TwitterSearchResponseDTO.Media> mediaList) {
        TwitterSearchResponseDTO.Media media = getMediaByUrl(url, mediaList);
        return media != null ? media.getType() : "photo";
    }

    /**
     * 根据原始URL获取媒体对象
     */
    private TwitterSearchResponseDTO.Media getMediaByUrl(String url, List<TwitterSearchResponseDTO.Media> mediaList) {
        if (mediaList == null) {
            return null;
        }
        for (TwitterSearchResponseDTO.Media media : mediaList) {
            String mediaUrl = media.getUrl() != null ? media.getUrl() : media.getPreviewImageUrl();
            if (url.equals(mediaUrl)) {
                return media;
            }
        }
        return null;
    }

    /**
     * 生成文件名
     */
    private String generateFileName(TwitterSearchResponseDTO.Media media) {
        String extension = getExtension(media);
        return UUID.randomUUID().toString() + "." + extension;
    }

    /**
     * 根据媒体类型获取扩展名
     */
    private String getExtension(TwitterSearchResponseDTO.Media media) {
        String url = media.getUrl() != null ? media.getUrl() : media.getPreviewImageUrl();
        if (url == null) {
            return "jpg";
        }

        // 从URL中提取扩展名
        if (url.contains(".jpg") || url.contains(".jpeg")) {
            return "jpg";
        } else if (url.contains(".png")) {
            return "png";
        } else if (url.contains(".gif")) {
            return "gif";
        } else if (url.contains(".mp4")) {
            return "mp4";
        }

        // 根据类型默认
        if ("video".equals(media.getType())) {
            return "mp4";
        } else if ("animated_gif".equals(media.getType())) {
            return "gif";
        }
        return "jpg";
    }

    /**
     * 下载媒体文件到上传路径
     * @return 可访问的URL路径（通过serverUrl组装）
     */
    private String downloadMedia(String mediaUrl, String fileName) {
        try {
            // 使用系统上传路径 + twitter/日期 作为保存目录
            String basePath = SopConfig.getUploadPath();
            String dateDir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String saveDir = basePath + "/twitter/" + dateDir;

            Path dirPath = Paths.get(saveDir);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            // 本地文件路径 - 使用 Paths.get 自动处理分隔符
            String localPath = Paths.get(saveDir, fileName).toString();

            // 下载文件（使用代理）
            URL url = new URL(mediaUrl);
            InputStream in;
            if (twitterApiV2Properties.isProxyEnabled()) {
                // 使用代理
                java.net.Proxy proxy = new java.net.Proxy(
                        java.net.Proxy.Type.HTTP,
                        new InetSocketAddress(twitterApiV2Properties.getProxyHost(),
                                twitterApiV2Properties.getProxyPort()));
                in = url.openConnection(proxy).getInputStream();
            } else {
                // 直接连接
                in = url.openStream();
            }

            try (ReadableByteChannel rbc = Channels.newChannel(in);
                 FileOutputStream fos = new FileOutputStream(localPath)) {
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            }

            // 转换为可访问的URL
            String accessUrl = convertToAccessUrl(localPath);
            log.info("媒体文件下载成功: {}, 访问URL: {}", localPath, accessUrl);
            return accessUrl;
        } catch (Exception e) {
            log.error("下载媒体文件失败: {}", mediaUrl, e);
            return null;
        }
    }

    /**
     * 将本地路径转换为可访问的URL
     * 例如: /home/ubuntu/uploadPath/upload/twitter/2026/03/31/xxx.jpg
     * 转为: http://192.168.1.7:8080/profile/upload/twitter/2026/03/31/xxx.jpg
     */
    private String convertToAccessUrl(String localPath) {
        String serverUrl = SopConfig.getServerUrl();

        // 处理路径分隔符，统一使用 /
        String normalizedPath = localPath.replace("\\", "/");

        // 提取 upload 之后的相对路径
        // /home/ubuntu/uploadPath/upload/twitter/2026/03/31/xxx.jpg
        // -> /upload/twitter/2026/03/31/xxx.jpg
        String relativePath = normalizedPath;
        int uploadIndex = normalizedPath.indexOf("/upload/");
        if (uploadIndex != -1) {
            relativePath = normalizedPath.substring(uploadIndex);
        }

        // 确保 relativePath 以 / 开头
        if (!relativePath.startsWith("/")) {
            relativePath = "/" + relativePath;
        }

        // 拼接 serverUrl 和 /profile + 相对路径
        if (serverUrl != null && !serverUrl.isEmpty()) {
            // 去掉 serverUrl 末尾的 /
            if (serverUrl.endsWith("/")) {
                serverUrl = serverUrl.substring(0, serverUrl.length() - 1);
            }
            return serverUrl + "/profile" + relativePath;
        }

        // 如果没有配置 serverUrl，返回带 /profile 前缀的路径
        return "/profile" + relativePath;
    }
}
