# ddns
腾讯云 DNS 解析刷新工具。
自动检测机器的公网 IP，并与腾讯云解析 IP 进行比较进行刷新操作。

## 使用
配置文件 application.yml
```
ddns:
  secret_id: your_secret_id
  secret_key: your_secret_key
  domain: your_domain
  record_id: your_record_id
```
