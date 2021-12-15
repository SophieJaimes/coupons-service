FROM java:8

COPY coupons-service-1.0.0.jar /home/app/
CMD java -Djava.security.egd=file:/dev/urandom -jar /home/app/coupons-service-1.0.0.jar

EXPOSE 8080

