$execArgs = ($args | ForEach-Object { $_ }) -join ' '
& mvn exec:java `
    "-Dexec.mainClass=com.finalProject.Main" `
    "-Dexec.args=$execArgs"