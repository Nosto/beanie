#!/bin/bash
mkdir -p config/certs
aws ssm get-parameter --name=development_ssl_dev.nos.to_private_key --with-decryption --output text --query Parameter.Value | sed 's/\\n/\n/g' > config/certs/dev.nos.to.key
aws acm get-certificate --certificate-arn=arn:aws:acm:us-east-1:652884599935:certificate/0a5d2bae-478d-4643-922c-37e04b1cde49 --output text > config/certs/dev.nos.to.crt
openssl pkcs12 -export -in config/certs/dev.nos.to.crt -inkey config/certs/dev.nos.to.key -out config/certs/keystore.p12 -name "spring" -passout pass:nosto
rm config/certs/*.crt
rm config/certs/*.key
