### Build project:
```mvn clean install```

### Start services:
```java -jar target/unlimited-0.0.1-SNAPSHOT.jar```


### deploy to aws
    - go to jenkins: http://10.206.8.20:8080/job/Pimsleur_Backend/job/UmlimitedBackend_Deploy/    (username: admin, password: pimsleur)
    - click build now
    - several seconds later, click "Select Parameters" in stage view
    - select jar file and environment
    - click deploy
    
##### DEV:
Instance:  i-0f6069ee82692ecbb (DEV_UnlimitedBackend)

Public DNS: ec2-54-165-165-197.compute-1.amazonaws.com

#### QA:
Instance:  i-0e7490d0b50185618 (QA_UnlimitedBackend)

Public DNS: ec2-54-89-109-156.compute-1.amazonaws.com

#### UAT:
Instance:  i-0e46cc08c799b1235 (UAT_UnlimitedBackend)

Public DNS: ec2-54-167-82-185.compute-1.amazonaws.com

#### PROD:
Instance:  i-0e4ef2be6ab7c0af5 (PROD_UnlimitedBackend)

Public DNS: ec2-18-233-65-171.compute-1.amazonaws.com