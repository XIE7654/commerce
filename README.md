使用 IDEA 打开 Terminal 终端，在 根目录 下直接执行 mvn clean install package '-Dmaven.test.skip=true' 命令。

如果执行报 Unknown lifecycle phase “.test.skip=true” 错误，使用 mvn clean install package -Dmaven.test.skip=true 即可。

ps：只有首次需要执行 Maven 命令，解决基础 pom.xml 不存在，导致报 BaseDbUnitTest 类不存在的问题。