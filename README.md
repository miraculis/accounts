# accounts
## technology used: 
for distributed cache - ignite, for data migration - flydb, for running and configuring - spring boot, for rest api - spring rest

## installation
1. Install node.js, jdk1.8, maven
2. npm install -g @angular/cli
3. cd ../accounts-gui; npm install

## running
1. cd ../accounts-gui 
2. ng build -prod
4. cd ../accounts
5. mvn package
6. java -jar targets/accounts-0.1.0.jar
7. goto localhost:8081
