## Yuansfer-Mobile-Pay-UniPlugin-Android
Yuansfer-Mobile-Pay-UniPlugin-Android是基于uni-app调用原生代码的插件项目，以json为数据交互方式。

### js通过native支付功能
1. 预下单(prepay),入参及回调：
```
jsonObject {
               "merchantNo":"200043",
               "storeNo":"300014",
               "token":"4dc2f2281d1f51fe137eafb914106524",
               "payType":1,
               "amount":0.01,
               "ipnUrl":"http://www.yuansfer.com",
               "reference":"12345",
               "note":"note",
               "description":"description",
               "terminal":"APP",
               "currency":"USD"
           }

jsCallback {
               "payType":1,
               "retCode":200,
               "retMsg":"payment success"
           }

```
2. 查询订单
```
jsonObject {
                "merchantNo":"200043",
                "storeNo":"300014",
                "token":"4dc2f2281d1f51fe137eafb914106524",
                "amount":0.01,
                "reference":"24324324"
            }

jsCallback {
               "retCode":200,
               "retMsg":"query success",
               "result":{
                   "reference":"test20180801006",
                   "transactionId":"297553569604475564",
                   "amount":"2.00",
                   "refundInfo":[
                       {
                           "refundTransactionId":"297553569604658581",
                           "refundAmount":"1.50"
                       },
                       {
                           "refundTransactionId":"297553569604680588",
                           "refundAmount":"0.50"
                       }
                   ],
                   "currency":"CNY",
                   "status":"success"
               }
           }
```
3. 退款
```
jsonObject {
               "merchantNo":"200043",
               "storeNo":"300014",
               "token":"4dc2f2281d1f51fe137eafb914106524",
               "reference":"23432434"
           }

jsCallback {
               "retCode":200,
               "retMsg":"refund success",
               "result":{
                   "amount":"0.01",
                   "status":"success",
                   "currency":"CNY",
                   "reference":"44444",
                   "refundAmount":"0.01",
                   "refundTransactionId":"297245675773380538",
                   "refundReference":"123123",
                   "oldTransactionId":"297245675773319174"
               }
           }
```

### 其它说明

状态码(retCode)
- 200 成功
- 201 取消
- 202 失败

支付类型(payType)
- 1 支付宝
- 2 微信

设置支付测试模式
- var payPlugin = uni.requireNativePlugin('YuansferPay');
- payPlugin.setDebug({debug:true})

### 版本日志
#### 0.1.0
- 项目初始化
- uni-app可调用native的预付款、订单详情、退款等功能