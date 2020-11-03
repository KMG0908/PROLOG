<?php
$dbName = $_POST['dbName'];

$DB['HOST'] = '192.168.43.10';
$DB['id'] = 'root';
$DB['pw'] = '1234';
$DB['db'] = $dbName;

$mysqli = new mysqli($DB['HOST'],$DB['id'],$DB['pw'],$DB['db']);

// 테이블의 모든 정보를 읽어온다.
$query = 'SELECT * FROM table3';
// 쿼리를 실행하고 그 결과값을 $result변수에 넣는다.
$result = $mysqli->query($query);
$total_record = $result->num_rows;

$result_array = array();

for ( $i = 0; $i < $total_record; $i++ ) {
  // 한행씩 읽기 위해 offset을 준다.
  $result->data_seek($i);

  // 결과값을 배열로 바꾼다.
  $row = $result->fetch_array();
  // 결과값들을 JSON형식으로 넣기 위해 연관배열로 넣는다.
  $row_array = array(
    "AttackLevel" => $row['AttackLevel'],
    "Problem" => $row['Problem'],
    "Country" => $row['Country'],
    "Date" => $row['Date'],
    "Time" => $row['Time'],
    "ClientIP" => $row['ClientIP'],
    "ServerIP" => $row['ServerIP'],
    "Port" => $row['Port']
    );
  // 한 행을 results에 넣을 배열의 끝에 추가한다.
  array_push($result_array, $row_array);
}

// 위에서 얻은 결과를 다시 JSON형식으로 넣는다.
$arr = array(
  "status" => "OK",
  "num_result" => "$total_record",
  "results" => $result_array
  );

// 만든건 그냥 배열이므로 JSON형식으로 인코딩을 한다.
$json_array = json_encode($arr);

// 인코딩한 JSON배열을 출력한다.
print_r($json_array);
?>
