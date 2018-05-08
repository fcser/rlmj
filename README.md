# -
基于虹软sdk的人脸识别门禁服务端

基于SSM

maven包管理

数据库是基于mysql

现有bug：
1服务器开一页，第二天就有问题： Cause: com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException: No operations allowed after connection closed.
; ]; No operations allowed after connection closed.; nested exception is com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException: No operations allowed after connection closed.] with root cause
java.net.SocketException: Software caused connection abort: recv failed
2异常处理没做好
