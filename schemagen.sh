#!/bin/bash

ant
rm -rf schemas
mkdir schemas

schemagen -cp common/build/classes -d schemas common/src/main/java/dk/frv/enav/common/xml/metoc/request/MetocForecastRequest.java
mv schemas/schema1.xsd schemas/metoc_req.xsd

schemagen -cp common/build/classes -d schemas common/src/main/java/dk/frv/enav/common/xml/metoc/response/MetocForecastResponse.java
mv schemas/schema1.xsd schemas/metoc_res.xsd

schemagen -cp common/build/classes -d schemas common/src/main/java/dk/frv/enav/common/xml/msi/request/MsiPollRequest.java
mv schemas/schema1.xsd schemas/msi_req.xsd

schemagen -cp common/build/classes -d schemas common/src/main/java/dk/frv/enav/common/xml/msi/response/MsiResponse.java
mv schemas/schema1.xsd schemas/msi_res.xsd

schemagen -cp common/build/classes -d schemas common/src/main/java/dk/frv/enav/common/xml/nogo/request/NogoRequest.java
mv schemas/schema1.xsd schemas/nogo_req.xsd

schemagen -cp common/build/classes -d schemas common/src/main/java/dk/frv/enav/common/xml/nogo/response/NogoResponse.java
mv schemas/schema1.xsd schemas/nogo_res.xsd
