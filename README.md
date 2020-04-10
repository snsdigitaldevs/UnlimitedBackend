### Build project:
```mvn clean install```

### Start services:
```java -jar target/unlimited-0.0.1-SNAPSHOT.jar```

### Deploy to aws ec2 host on local:
1. copy private key file to project root path and rename as pimsleur.pem
2. run below commands
```
chmod 400 ./pimsleur.pem
./jenkinsfiles/scripts/deploy.sh {env}   #DEV||QA||ITG||UAT
```

### deploy to aws
    - go to jenkins: http://10.206.8.4:8080/job/Pimsleur_Backend/job/UmlimitedBackend_Deploy/    (username: admin, password: pimsleur)
    - click build now
    - several seconds later, click "Select Parameters" in stage view
    - select jar file and environment
    - click deploy
    
##### DEV:
Instance:  i-0f6069ee82692ecbb (DEV_UnlimitedBackend)

Public DNS: ec2-34-238-227-224.compute-1.amazonaws.com

#### QA:
Instance:  i-0e7490d0b50185618 (QA_UnlimitedBackend)

Public DNS: ec2-34-232-139-149.compute-1.amazonaws.com

#### ITG:
Instance: i-0e46cc08c799b1235 (ITG_UnlimitedBackend)

Public DNS: ec2-34-229-55-156.compute-1.amazonaws.com

#### UAT:
Instance:  i-0e46cc08c799b1235 (UAT_UnlimitedBackend)

Public DNS: ec2-54-156-176-22.compute-1.amazonaws.com

#### PROD:
Instance:  i-0e4ef2be6ab7c0af5 (PROD_UnlimitedBackend)

Public DNS: ec2-18-233-65-171.compute-1.amazonaws.com

### Prod env logs

For diagnostic purposes, we can check logs of prod env in AWS CloudWatch logs.

Log into AWS console, go to CloudWatch - Log Groups - Streams for /var/log/messages.

Logs are coming from 2 EC2 instances that are running our java code, and also coming from ELB (the access log).

You can use the "Search log group" function provided by AWS to search across all log streams under this log group.
