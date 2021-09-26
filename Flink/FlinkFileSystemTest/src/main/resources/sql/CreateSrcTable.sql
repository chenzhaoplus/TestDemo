CREATE TABLE csvin (
`测试人员` string,
`TAPD编号` string,
`项目名称` string,
`任务名称` string,
`转测试时间` string,
`状态` string,
`测试进度 （0-100%）` string,
`中止原因` string,
`任务对应负责开发` string,
`备注 (测试过程中遇到的问题)` string,
`待提测` string
) WITH (
'connector' = 'filesystem',
'path' = 'hdfs://master.dev.smcaiot.com:8020/csv/input/45s6d4fs6f16ertwfw',
'format' = 'csv'
)
