### Build project:
```mvn clean install```

### Start services:
```java -jar target/unlimited-0.0.1-SNAPSHOT.jar```


### deploy to aws
    - go to jenkins: http://10.206.8.7:8080/job/Pimsleur_Backend/job/UmlimitedBackend_Deploy/    (username: admin, password: pimsleur)
    - click build now
    - several seconds later, click "Select Parameters" in stage view
    - select jar file and environment
    - click deploy
    
