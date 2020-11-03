<?php

   $file = fopen("test.txt","a");
   fwrite($file,$_GET["usage"].":");
   fwrite($file,$_GET["threads"].":");
   fwrite($file,$_GET["alert"]."\r\n");
   fclose($file)
?>
