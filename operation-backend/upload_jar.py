import paramiko
import time
###################################################
###################################################
###################################################
###################################################
port=22
username="root"




hostname="xxxxx"
password='xxxxxxx'

file_name="operation-backend-0.0.1-SNAPSHOT.jar"
###################################################
###################################################
###################################################
###################################################

transport = paramiko.Transport((hostname,port))
transport.connect(username=username,password=password)

sftp = paramiko.SFTPClient.from_transport(transport)
# ��jar �ϴ��������� /tmp/jar
sftp.put('./target/'+file_name, '/root/'+file_name)
transport.close()


# ����SSH����
ssh = paramiko.SSHClient()
# �������Ӳ���know_hosts�ļ��е�����
ssh.set_missing_host_key_policy(paramiko.AutoAddPolicy())
# ���ӷ�����
ssh.connect(hostname=hostname, port=port, username=username, password=password,allow_agent=False,look_for_keys=False)
# ִ������
cmd="ps -ef|grep 'java -jar "+file_name+"'|awk '{print $2}'"
print(cmd)
stdin, stdout, stderr = ssh.exec_command(cmd)
# ��ȡ������
result = stdout.read()
pids=result.decode('utf-8').split("\n")
for pid in pids:
	# kill����
	if len(pid)>0:
		cmd="kill -9 "+pid
		print(cmd)
		stdin, stdout, stderr=ssh.exec_command(cmd)
# ��������
cmd="nohup java -jar "+file_name+" --spring.profiles.active=test  >/dev/null 2>&1 & \n"
print(cmd)
chan = ssh.invoke_shell()
chan.send(cmd)
time.sleep(1)
# �ر�����
ssh.close()

print("SUCCESS")
