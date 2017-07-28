<?php
//
// 保存客户端上报的日志到数据库
//
// 客户端使用POST请求发送日志
// logtype -- 日志类型 string : 牌局记录 / 网络状态变化
// logdata -- 日志数据 string : 详细的日志内容

// 数据库配置
$db_host = "localhost";
$db_database = "clientlog";
$db_user = "root";
$db_passwd = "kod@2011";

$client_logtype = $_POST["logtype"];
$client_logdata = $_POST["logdata"];

if ($_SERVER['REQUEST_METHOD'] != "POST") {
    printf("invalid request");
    exit();
}

$datetime = new DateTime;
$log_time = $datetime->format('Y-m-d H:i:s');

$mysqli = new mysqli($db_host, $db_user, $db_passwd, $db_database);
if (mysqli_connect_error()) {
    printf("Connect failed: %s\n", mysqli_connect_error());
} else {
    $stmt = $mysqli->prepare("INSERT INTO `clientlog` (`logtype`,`logtime`,`logdata`) VALUES (?, ?, ?)");
    $stmt->bind_param('sss', $client_logtype, $log_time, $client_logdata);

    $stmt->execute();
    printf("%d Row inserted.\n", $stmt->affected_rows);

    $stmt->close();
    $mysqli->close();
}

?>
