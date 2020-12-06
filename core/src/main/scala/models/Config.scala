package models

class Config(
    val kafkaHost: String,
    val kafkaPort: String,
    val kafkaRecordTopic: String,
    val csvPath: String,
    val kafkaAlertTopic: String,
    val csvPathAlert: String
)
