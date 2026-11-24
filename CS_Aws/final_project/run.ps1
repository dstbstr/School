$execArgs = ($args | ForEach-Object { 
    if($_ -match '\s') {"'" + $_ + "'"} else { $_ } 
}) -join ' '
& mvn exec:java `
    "-Dexec.mainClass=com.finalProject.Main" `
    "-Dexec.args=$execArgs"