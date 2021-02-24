<?php
class WhoKnows{
    private $tag = "WhoKnows";
    private $mysqli;
    private $query;
    public $host;
    public $user;
    public $pass;
    public $db;
    public $port;
    public $table;
    public $tables;
    public $param;
    public $container;
    public $container_next_1;
    public $operation;

    function __construct($password, $passed_db, $passed_object){
        $_table = json_decode($passed_object, true)['table'];

        $this->host = "localhost";
        $this->user = "root";
        $this->pass = $password;
        $this->db = $passed_db;
        $this->port = 8889; // 3306;
        $this->operation = json_decode($passed_object, true)['about'];
        $this->table = $_table['name'][0];
        $this->tables = $_table['name'];
        $this->param = isset($_table['param']) ? $_table['param'] : '';
        $this->container = isset($_table['container']) ? $_table['container'] : array();
        $this->container_next_1 = isset($_table['container_next_1']) ? $_table['container_next_1'] : array();
        
        header('Content-Type: application/json');
        error_reporting(0);
    }

    function onOperation($operation){
        ## WIKI // $tables[0] == $this->staff->tables[0]
        $rows = array(); //$row = $query->fetch_assoc(); //$str_col = //implode("`, `", $this->stuff->container->obj);
        $str_col = implode("`, `", array_keys($this->container));
        $str_row = implode("', '", array_values($this->container));
        $str_col_next_1 = implode("`, `", array_keys($this->container_next_1));
        $str_row_next_1 = implode("', '", array_values($this->container_next_1));
        $alert_content = implode(", ", array_slice($this->container, 0, 2)).',..';
        $_table_next_1 = $this->tables[1];
        $_table_next_2 = $this->tables[2];
        $_table_next_3 = $this->tables[3];

        $str_col_1_container = array_keys($this->container)[0];
        $str_col_2_container = array_keys($this->container)[1];
        $str_row_1_container = array_values($this->container)[0];
        $str_row_2_container = array_values($this->container)[1];
        
        if($operation == "fetch_sign_in"){
            if(stripos($str_col, '@') !== FALSE){
                $this->onDatabase("SELECT * FROM $this->table WHERE (`email` = '$str_col' AND `password` = '$str_row')", false);
            }else{
                $this->onDatabase("SELECT * FROM $this->table WHERE (`username` = '$str_col' AND `password` = '$str_row')", false);
            }
            
            while($row = $this->query->fetch_assoc()){
                $rows[] = $row;
            }
            
            echo $this->onComplete(true, $rows);
        }else if($operation == "fetch_only"){
            $this->onDatabase("SELECT * FROM $this->table", false);
            while($row = $this->query->fetch_assoc()){
                $rows[] = $row;
            }
            
            echo $this->onComplete(true, $rows);
        }else if($operation == "fetch_single"){
            $this->onDatabase("SELECT * FROM $this->table WHERE `$str_col` = '$str_row'", false);
            
            while($row = $this->query->fetch_assoc()){
                $rows[] = $row;
            }
            
            echo $this->onComplete(true, $rows);
        }else if($operation == "fetch_couple_by"){
            $this->onDatabase("SELECT * FROM $this->table WHERE (`$str_col_1_container` = '$str_row_1_container' AND `$str_col_2_container` = '$str_row_2_container')", false);
            
            while($row = $this->query->fetch_assoc()){
                $rows[] = $row;
            }
            
            echo $this->onComplete(true, $rows);
        }else if($operation == "fetch_join"){
            $this->onDatabase("SELECT * FROM $this->table INNER JOIN $_table_next_1 ON $_table_next_1.$this->param = $this->table.$this->param", false);

            while($row = $this->query->fetch_assoc()){
                $rows[] = $row;
            }

            echo $this->onComplete(true, $rows);
        }else if($operation == "fetch_join_couple_by"){
            $this->onDatabase("SELECT * FROM $this->table 
            INNER JOIN $_table_next_1 ON $_table_next_1.`$str_col_1_container` = $this->table.`$str_col_1_container`
            INNER JOIN $_table_next_2 ON $_table_next_2.`$str_col_2_container` = $this->table.`$str_col_2_container` 
            WHERE 
                $this->table.`$str_col_1_container` = '$str_row_1_container' AND 
                $this->table.`$str_col_2_container` = '$str_row_2_container'", false);

            while($row = $this->query->fetch_assoc()){
                $rows[] = $row;
            }

            echo $this->onComplete(true, $rows);
        }else if($operation == "fetch_join_couple_by_param"){
            $this->onDatabase("SELECT * FROM $this->table 
            INNER JOIN $_table_next_1 ON $_table_next_1.`$str_col_1_container` = $this->table.`$str_col_1_container`
            INNER JOIN $_table_next_2 ON $_table_next_2.`$str_col_2_container` = $this->table.`$str_col_2_container` 
            $this->param", false);

            while($row = $this->query->fetch_assoc()){
                $rows[] = $row;
            }

            echo $this->onComplete(true, $rows);
        }else if($operation == "fetch_join_by"){
            $this->onDatabase("SELECT * FROM $this->table 
            INNER JOIN $_table_next_1 ON $_table_next_1.`$str_col_1_container` = $this->table.`$str_col_1_container`
            WHERE $this->table.`$str_col_1_container` = '$str_row_1_container'", false);

            while($row = $this->query->fetch_assoc()){
                $rows[] = $row;
            }

            echo $this->onComplete(true, $rows);
        }else if($operation == "fetch_join_couple"){
            $this->onDatabase("SELECT * FROM $this->table 
            INNER JOIN $_table_next_1 ON $_table_next_1.`$str_col_1_container` = $this->table.`$str_col_1_container`
            INNER JOIN $_table_next_2 ON $_table_next_2.`$str_col_2_container` = $this->table.`$str_col_2_container`", false);

            while($row = $this->query->fetch_assoc()){
                $rows[] = $row;
            }

            echo $this->onComplete(true, $rows);
        }else if($operation == "post_only"){
            $this->onDatabase("INSERT INTO $this->table (`$str_col`) VALUES ('$str_row')", false);

            if($this->query){
                echo $this->onComplete(true, "$alert_content added successfully.");
            }
        }else if($operation == "post_couple"){
            $this->onDatabase("INSERT INTO $this->table (`$str_col`) VALUES ('$str_row'); INSERT INTO $_table_next_1 (`$str_col_next_1`) VALUES ('$str_row_next_1')", true);

            if($this->query){
                echo $this->onComplete(true, "couple data added successfully.");
            }
        }else if($operation == "update_post_couple"){
            $quentainer = $this->objectToStrQuery(array_keys($this->container), array_values($this->container));
            // $this->onDatabase("SELECT $quentainer AS nope", false); echo $this->onComplete(true, $this->query->fetch_assoc()['nope']);

            $this->onDatabase("UPDATE $this->table SET $quentainer WHERE $this->param; INSERT INTO $_table_next_1 (`$str_col_next_1`) VALUES ('$str_row_next_1')", true);

            if($this->query){
                echo $this->onComplete(true, "couple data added successfully.");
            }
        }else if($operation == "post_trigger"){
        }else if($operation == "update_trigger"){
        }else if($operation == "update_only"){
            $quentainer = $this->objectToStrQuery(array_keys($this->container), array_values($this->container));
            
            $this->onDatabase("UPDATE $this->table SET $quentainer WHERE $this->param", false);
            
            if($this->query){
                echo $this->onComplete(true, "$alert_content updated successfully.");
            }

        }else if($operation == "delete"){
            $this->onDatabase("DELETE FROM $this->table WHERE $str_col = $str_row", false);

            echo $this->onComplete(true, "$str_row delete successfully");
        }else if($operation == "delete_couple"){
            $this->onDatabase("DELETE FROM $this->table WHERE `$str_col` = '$str_row'; DELETE FROM $_table_next_1 WHERE `$str_col` = '$str_row';", true);

            echo $this->onComplete(true, "$str_row delete successfully");
        }else if($operation == "delete_triple"){
            $this->onDatabase("DELETE FROM $this->table WHERE `$str_col` = '$str_row'; DELETE FROM $_table_next_1 WHERE `$str_col` = '$str_row'; DELETE FROM $_table_next_2 WHERE `$str_col` = '$str_row';", true);

            echo $this->onComplete(true, "$str_row delete successfully");
        }else if($operation == "delete_quad"){
            $this->onDatabase("DELETE FROM $this->table WHERE `$str_col` = '$str_row'; DELETE FROM $_table_next_1 WHERE `$str_col` = '$str_row'; DELETE FROM $_table_next_2 WHERE `$str_col` = '$str_row';DELETE FROM $_table_next_3 WHERE `$str_col` = '$str_row';", true);

            echo $this->onComplete(true, "$str_row delete successfully");
        }else{
            $this->onDatabase("SELECT 'Invalid operation on server $this->db' AS nope", false);

            echo $this->onComplete(true, $this->query->fetch_assoc()['nope']);
        }
    }

    function onDatabase($getOperation, $isMulti){
        $this->mysqli = new mysqli($this->host, $this->user, $this->pass, $this->db, $this->port);
        if($isMulti){
            $this->query = $this->mysqli->multi_query($getOperation);
        }else{
            $this->query = $this->mysqli->query($getOperation);
        }

        if(mysqli_connect_errno()) echo $this->onComplete(false, mysqli_connect_error());
        else if(!$this->query) echo $this->onComplete(false, $this->mysqli->error);
    }

    function objectToStrQuery($keys, $values) {
        $a = 0; $b = 0; $idx = 0;
        $pop = array();
        $result = array();

        while($a < count($keys) && $b < count($values)){ // odd even -> ($key % 2 == 0)
            $result[$idx++] = $keys[$a++]."` = '";
            $result[$idx++] = $values[$b++]."', `";
        }

        return rtrim(("`".implode("", $result)), ", `");
    }
    
    function onComplete($isSuccess, $data){
        if(preg_match("/\bforeign key constraint fails\b/", strtolower($data))){
            $data = "foreign key is not exist.";
        } else if (preg_match("/\bduplicate\b/", strtolower($data))){
            $data = "primary key already exist.";
        }
        
        //$result = array("about" => str_replace("_", " ", $this->operation), "complete" => $isSuccess, "result" => array($this->table => $data));

        return json_encode($data, JSON_PRETTY_PRINT);
    }
}

$fetchJoinCoupleParamBy = '{
    "about": "fetch_join_couple_by_param",
    "database": "_who_knows",
    "table": {
        "name": ["_table_room", "_table_quiz" , "_table_user"],
        "param": "expired = 0 LIMIT 10",
        "container":{
            "roomId": null,
            "userId": null
        }
    }
}';

$case = file_get_contents('php://input'); $fetchJoinCoupleParamBy;

$requests = json_decode($case);
$whoKnows = new WhoKnows("root", $requests->database, $case);

if($_SERVER['REQUEST_METHOD'] == 'POST'){
    echo $whoKnows->onOperation($requests->about); //echo explodeJson($objUpdate);
}else {
    echo $whoKnows->onComplete(false, 'Access Forbidden.');
}

?>
