<?php
file_put_contents("/tmp/upload.tar.gz", file_get_contents('php://input'));
echo shell_exec ("rm -rf /var/www/html/static");
echo shell_exec ("tar -xvf /tmp/upload.tar.gz -C /var/www/html");
?>
