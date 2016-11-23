$user = $_POST['Usuario'];
$pass = $_POST['Contrasena'];

$link = mysql_connect('localhost', 'root', 'guyozz');
if (!$link) {
    die('No pudo conectar: ' . mysql_error());
}

mysql_select_db("Geomatica");

$q=mysql_query("SELECT * FROM Usuarios WHERE Usuario='{$user}' AND Contrasena='{$pass}' ");

while($e=mysql_fetch_assoc($q))

              $output[]=$e;

      print(json_encode($output));

mysql_close($link);
?>
