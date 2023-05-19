```
GET _search
{
  "query": {
    "match_all": {}
  }
}
#创建索引
PUT /hotel
{
  "settings": {
    "number_of_replicas": 1,
    "number_of_shards": 2
  },
  "mappings": {
    "properties": {
      "title": {
        "type": "text"
      },
      "city": {
        "type": "keyword"
      },
      "price": {
        "type": "double"
      },
      "star": {
        "type": "integer"
      }
    }
  }
}
#获取索引信息
GET /hotel/_mapping
#删除索引
DELETE /hotel
#关闭索引 -- 关闭后索引不可操作【插入｜查询 ···】
POST /hotel/_close
#开启索引 
POST /hotel/_open
#索引中添加字段
#date指定数据类型 "format":"yyyy-MM-dd HH:mm:ss" 
POST /hotel/_mapping
{
  "properties": {
    "tag": {
      "type": "keyword"
    },
    "full_room": {
      "type": "boolean"
    },
    "create_time": {
      "type": "date",
      "format": "yyyy-MM-dd HH:mm:ss"
    }
  }
}
#添加地理类型
POST /hotel/_mapping
{
  "properties": {
    "location": {
      "type": "geo_point"
    }
  }
}
#属性添加属性子类型【fields】 
PUT /hotel_order
{
  "mappings": {
    "properties": {
      "order_id": {
        "type": "keyword"
      },
      "user_id": {
        "type": "keyword"
      },
      "user_name": {
        "type": "text",
        "fields": {
          "user_name_keyword": {
            "type": "keyword"
          }
        }
      },
      "hotel_id": {
        "type": "keyword"
      }
    }
  }
}



#插入文档 
POST /hotel/_doc/001
{
  "title": "似水流年的小窝",
  "city": "厦门",
  "price": "520",
  "star": 4
}
#查询文档 
GET /hotel/_doc/001

##别名操作 #创建三个索引
PUT /january_log
{
  "mappings": {
    "properties": {
      "uid": {
        "type": "keyword"
      },
      "hotel_id": {
        "type": "keyword"
      },
      "check_in_date": {
        "type": "keyword"
      }
    }
  }
}
PUT /february_log
{
  "mappings": {
    "properties": {
      "uid": {
        "type": "keyword"
      },
      "hotel_id": {
        "type": "keyword"
      },
      "check_in_date": {
        "type": "keyword"
      }
    }
  }
}
PUT /march_log
{
  "mappings": {
    "properties": {
      "uid": {
        "type": "keyword"
      },
      "hotel_id": {
        "type": "keyword"
      },
      "check_in_date": {
        "type": "keyword"
      }
    }
  }
}
#分别插入数据
POST /january_log/_doc/001
{
  "uid": "001",
  "hotel_id": "92772",
  "check_in_date": "2021-01-05"
}
POST /february_log/_doc/001
{
  "uid": "001",
  "hotel_id": "92772",
  "check_in_date": "2021-01-05"
}
POST /march_log/_doc/001
{
  "uid": "001",
  "hotel_id": "92772",
  "check_in_date": "2021-01-05"
}
#创建别名 三个索引使用同一个别名
POST /_aliases
{
  "actions": [
    {
      "add": {
        "index": "january_log",
        "alias": "last_three_month"
      }
    },
    {
      "add": {
        "index": "february_log",
        "alias": "last_three_month"
      }
    },
    {
      "add": {
        "index": "march_log",
        "alias": "last_three_month"
      }
    }
  ]
}
#搜索别名 查出三个索引的数据
GET /last_three_month/_search
{
  "query": {
    "term": {
      "uid": "002"
    }
  }
}
#PS:别名对应一个索引时操作可以执行插入操作，对应多个索引时不行
POST /last_three_month/_doc/002
{
  "uid": "002",
  "hotel_id": "92773",
  "check_in_date": "2021-01-05"
}
#设定别名插入数据时将插入索引【january_log】中 
POST /_aliases
{
  "actions": [
    {
      "add": {
        "index": "january_log",
        "alias": "last_three_month",
        "is_write_index": true
      }
    }
  ]
} 
#删除索引的别名
POST /_aliases
{
  "actions": [
    {
      "remove": {
        "index": "january_log",
        "alias": "last_three_month"
      }
    }
  ]
}

#写入[数组型]数据
POST /hotel/_doc/002
{
  "title": "旅窝",
  "city": "日照",
  "price": 528,
  "tag": [
    "有车位",
    "免费WIFI",
    "海鲜"
  ],
  "star": 4,
  "create_time": "2020-01-01 12:30:00"
}
#写入对象型数据 
POST /hotel/_doc/003
{
  "title": "心窝",
  "city": "上海",
  "price": 258,
  "tag": [
    "有车位",
    "免费WIFI",
    "happy"
  ],
  "star": 5,
  "create_time": "2020-01-01 12:30:00",
  "comment_info": {
    "properties": {
      "favouerable_comment": 199,
      "negative_comment": 1,
      "top3_favourable_comment": {
        "top1": {
          "content": "干净整洁又卫生",
          "score": 98
        },
        "top2": {
          "content": "服务周到，停车方便，Wi-Fi免费",
          "score": 89
        },
        "top3": {
          "content": "闹中取静，环境优美",
          "score": 90
        }
      }
    }
  }
}
#写入位置型数据
POST /hotel/_doc/004
{
  "title": "文雅酒店",
  "city": "北京",
  "price": 556,
  "tag": [
    "豆汁",
    "爆肚",
    "铜锅涮肉"
  ],
  "star": 3,
  "create_time": "2022-01-01 12:00:00",
  "location": {
    "lat": 40.012134,
    "lon": 116.497553
  }
}

GET /hotel/_mapping 

GET /hotel/_search
{
  "query": {
    "match_all": {}
  }
}

#组合查询
GET /demo/_mapping
POST /demo/_bulk
{"index":{"_index":"demo"}}
{"title":"中印边防军于拉达克举行会晤强调维护边境和平","tag":["军事"],"publishTime":"2018-01-27T08:34:00Z"}     
{"index":{"_index":"demo"}}
{"title":"费德勒收郑泫退赛礼进决赛战西里奇","tag":["体育","网球"],"publishTime":"2018-01-26T14:34:00Z"} 
{"index":{"_index":"demo"}}
{"title":"欧文否认拿动手术威胁骑士兴奋全明星联手詹皇","tag":["体育","篮球"],"publishTime":"2018-01-26T08:34:00Z"}
{"index":{"_index":"demo"}}
{"title":"皇马官方通告拉莫斯伊斯科伤情将缺阵西甲关键战","tag":["体育","足球"],"publishTime":"2018-01-26T20:34:00Z"} 
POST /demo/_search
{
  "from": 0,
  "size": 10,
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "title": "赛"
          }
        },
        {
          "term": {
            "tag.keyword": "体育"
          }
        }
      ]
    }
  },
  "highlight": {
    "fields": {
      "title": {
        "pre_tags": "",
        "post_tags": ""
      }
    }
  }
}

GET /demo/_explain/iq6NM4gB6-vEubt2yyI2
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "title": "费德勒"
          }
        },
        {
          "term": {
            "tag.keyword": "网球"
          }
        },
        {
          "range": {
            "publishTime": {
              "gte": "2018-01-26T08:00:00Z",
              "lte": "2018-01-26T20:00:00Z"
            }
          }
        }
      ]
    }
  }
}

#条件查询
#term 精准匹配
GET /demo_hotel/_search
{
  "query": {
    "term": {
      "title": {
        "value": "来"
      }
    }
  }
}
#match模糊匹配
GET /demo_hotel/_search
{
  "query": {
    "match_all": {}
  }
} 
POST /demo_hotel/_update/GBhwz4MBiGu3PXaWyuFI
{
  "doc": {
    "price": 188
  }
}

#更新文档
POST /demo_hotel/_update/IhiAz4MBiGu3PXaWHeHZ
{
  "doc": {
    "title": "好再来酒店"
  }
} 
#根据条件更新索引中指定字段 
POST /demo_hotel/_update_by_query
{
  "query": {
    "match": {
      "title": "张三"
    }
  },
  "script": {
    "source": "ctx._source['city']='上海'",
    "lang": "painless"
  }
}
#更新索引中指定字段（全局更新） 
POST /demo_hotel/_update_by_query
{
  "script": {
    "source": "ctx._source['city']='上海'",
    "lang": "painless"
  }
}
POST /demo_hotel/_doc/02
{
  "title": "小晓酒店",
  "city": "厦门",
  "price": "258"
}
#删除文档 
DELETE /demo_hotel/_doc/IhiAz4MBiGu3PXaWHeHZ
#根据条件删除文档 
DELETE /demo_hotel/_delete_by_query
{
  "query": {
    "term": {
      "title": {
        "value": "小晓酒店"
      }
    }
  }
}

#查找包涵查询值的数据，不会计算评分
POST /demo_hotel/_search
{
  "query": {
    "constant_score": {
      "filter": {
        "match": {
          "title": "酒店"
        }
      },
      "boost": 1.2
    }
  }
} 
POST /demo_hotel/_search
{
  "query": {
    "function_score": {
      "query": {
        "match": {
          "title": "酒店"
        }
      },
      "functions": [
        {
          "random_score": {}
        }
      ],
      "score_mode": "avg"
    }
  }
}

GET /bidding_simple_list/_mapping

#复杂组合查询
POST /bidding_simple_list/_search
{
  "from": 0,
  "size": 20,
  "query": {
    "bool": {
      "should": [
        {
          "bool": {
            "must": [
              {
                "term": {
                  "FIELD": {
                    "value": "VALUE"
                  }
                }
              },
              {
                "exists": {
                  "field": "contractAmount"
                }
              }
            ]
          }
        },
        {
          "range": {
            "contractAmount": {
              "gte": 10,
              "lte": 20
            }
          }
        }
      ]
    }
  }
}

#以项目为维度查询
POST /bidding_simple_list/_search
{
  "size": 0,
  "query": {
    "bool": {
      "filter": [
        {
          "term": {
            "evn": "test1"
          }
        },
        {
          "term": {
            "unitId": "9305"
          }
        }
      ]
    }
  },
  "aggregations": {
    "date_agg": {
      "date_range": {
        "field": "createAt",
        "format": "yyyy-MM-dd HH:mm:ss",
        "ranges": [
          {
            "to": "now+1d/d",
            "key": "all_time"
          },
          {
            "from": "now-2w/d",
            "to": "now+1d/d",
            "key": "two_week"
          }
        ]
      },
      "aggs": {
        "project_agg": {
          "terms": {
            "field": "projectId"
          },
          "aggs": {
            "estimatedPrice_sum": {
              "sum": {
                "field": "estimatedPrice"
              }
            },
            "top1": {
              "top_hits": {
                "_source": [
                  "projectName"
                ],
                "size": 1
              }
            },
            "bidderStatus_group": {
              "terms": {
                "field": "bidderStatus",
                "include": "SIGN_ELECTRONIC_SIGN_SUCCESS"
              },
              "aggs": {
                "contractAmount_sum": {
                  "sum": {
                    "field": "contractAmount"
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}

POST /bidding_simple_list/_search
{
  "query": {
    "bool": {
      "filter": [
        {
          "term": {
            "evn": {
              "value": "test1"
            }
          }
        },
        {
          "term": {
            "unitId": {
              "value": "9305"
            }
          }
        }
      ],
      "must_not": [
        {
          "exists": {
            "field": "publisher"
          }
        }
      ]
    }
  }
}

#以发布人为维度查询
POST /bidding_simple_list/_search
{
  "size": 0,
  "query": {
    "bool": {
      "filter": [
        {
          "term": {
            "evn": {
              "value": "test1"
            }
          }
        },
        {
          "term": {
            "unitId": {
              "value": "9305"
            }
          }
        },
        {
          "exists": {
            "field": "publisher"
          }
        }
      ]
    }
  },
  "aggs": {
    "date_agg": {
      "date_range": {
        "field": "createAt",
        "format": "yyyy-MM-dd HH:mm:ss",
        "ranges": [
          {
            "to": "now+1d/d",
            "key": "any_time"
          }
        ]
      },
      "aggs": {
        "publisher_agg": {
          "terms": {
            "field": "publisher",
            "missing": "--"
          },
          "aggs": {
            "estimatedPrice_sum": {
              "sum": {
                "field": "estimatedPrice"
              }
            },
            "bidderStatus_agg": {
              "terms": {
                "field": "bidderStatus",
                "include": [
                  "SIGN_ELECTRONIC_SIGN_SUCCESS"
                ]
              },
              "aggs": {
                "contractAmount_sum": {
                  "sum": {
                    "field": "contractAmount"
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}

#招标模式 环形图 
POST /bidding_simple_list/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "term": {
            "evn": {
              "value": "test1"
            }
          }
        },
        {
          "term": {
            "unitId": 9213
          }
        }
      ]
    }
  },
  "size": 0,
  "aggs": {
    "date_agg": {
      "date_range": {
        "field": "createAt",
        "format": "yyyy-MM-dd HH:mm:ss",
        "ranges": [
          {
            "key": "all_time",
            "to": "now+1d/d-1s"
          }
        ]
      },
      "aggs": {
        "aaa_agg": {
          "terms": {
            "field": "biddingPattern"
          }
        }
      }
    }
  }
}
DELETE /bidding_simple_list/_doc/ZB221104102231409
POST /bidding_simple_list/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "term": {
            "evn": {
              "value": "test1"
            }
          }
        },
        {
          "term": {
            "unitId": {
              "value": "9213"
            }
          }
        }
      ]
    }
  }
}
#预估造价
POST /bidding_simple_list/_search
{
  "size": 0,
  "query": {
    "bool": {
      "must": [
        {
          "term": {
            "evn": {
              "value": "test1"
            }
          }
        },
        {
          "term": {
            "unitId": {
              "value": "9213"
            }
          }
        },
        {
          "exists": {
            "field": "estimatedPrice"
          }
        }
      ]
    }
  },
  "aggs": {
    "date_agg": {
      "date_range": {
        "field": "createAt",
        "ranges": [
          {
            "to": "now+1d/d",
            "key": "all_time"
          }
        ]
      },
      "aggs": {
        "estimatedPrice_range": {
          "range": {
            "field": "estimatedPrice",
            "ranges": [
              {
                "to": 10,
                "key": "10W"
              },
              {
                "from": 10,
                "to": 50,
                "key": "10w50w"
              },
              {
                "from": 50,
                "to": 100,
                "key": "50w100w"
              },
              {
                "from": 100,
                "to": 500,
                "key": "100w~500w"
              },
              {
                "from": 500,
                "key": "500w+"
              }
            ]
          }
        }
      }
    }
  }
}

#按阶段统计 
POST /bidding_simple_list/_search
{
  "size": 0,
  "query": {
    "bool": {
      "filter": [
        {
          "term": {
            "evn": "test1"
          }
        },
        {
          "term": {
            "unitId": "9305"
          }
        }
      ],
      "should": [
        {
          "terms": {
            "stage": [
              "POST",
              "QUOTE",
              "CALIBRATION"
            ]
          }
        },
        {
          "bool": {
            "filter": {
              "term": {
                "stage": "SIGN"
              }
            },
            "must_not": [
              {
                "terms": {
                  "bidderStatus": [
                    "SIGN_ELECTRONIC_SIGN_SUCCESS",
                    "SIGN_ELECTRONIC_SIGN_FAIL"
                  ]
                }
              }
            ]
          }
        }
      ],
      "minimum_should_match": 1
    }
  },
  "aggs": {
    "stage_agg": {
      "terms": {
        "field": "stage"
      },
      "aggs": {
        "top6_agg": {
          "top_hits": {
            "size": 6,
            "_source": {
              "includes": [
                "biddingId",
                "biddingName",
                "biddingType",
                "biddingPattern",
                "publisher",
                "postNoticeDate",
                "operationType",
                "declareFormId",
                "createBy"
              ]
            },
            "sort": [
              {
                "createAt": "desc"
              }
            ]
          }
        }
      }
    }
  }
} 
#统计今天和昨天的最近4条项目数据 
POST /bidding_simple_list/_search
{
  "size": 0,
  "query": {
    "bool": {
      "filter": [
        {
          "term": {
            "evn": "test1"
          }
        },
        {
          "term": {
            "unitId": 9305
          }
        },
        {
          "range": {
            "createAt": {
              "gte": "now-1d/d",
              "lte": "now+1d/d"
            }
          }
        },
        {
          "exists": {
            "field": "biddingId"
          }
        }
      ]
    }
  },
  "aggs": {
    "date_agg": {
      "date_range": {
        "field": "createAt",
        "ranges": [
          {
            "from": "now-1d/d",
            "to": "now/d",
            "key": "yestoday"
          },
          {
            "from": "now/d",
            "to": "now+1d/d",
            "key": "today"
          }
        ]
      },
      "aggs": {
        "top4_agg": {
          "top_hits": {
            "size": 4,
            "_source": [
              "biddingId",
              "biddingName",
              "projectName",
              "publisher",
              "publishAnnouncementTime"
            ],
            "sort": [
              {
                "publishAnnouncementTime": {
                  "order": "desc"
                }
              }
            ]
          }
        }
      }
    }
  }
}
```
