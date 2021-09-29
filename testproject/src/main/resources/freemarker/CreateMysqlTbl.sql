CREATE TABLE if not exists `${tableName}` (
  ${cols}
) WITH (
   'connector' = 'jdbc',
   'url' = 'jdbc:mysql://172.16.4.83:3306/dmg?useSSL=false&characterEncoding=utf-8&serverTimezone=GMT%2B8',
   'table-name' = '${tableName}'
)
