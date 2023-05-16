#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

function switch_proxy() {
    IDLE_PORT=$(find_idle_port)

    echo "> 전환할 Port: $IDLE_PORT"
    echo "> Port 전환"
    # 아래 줄은 보면 echo를 통해서 나온 결과를 | 파이프라인을 통해서 service-url.inc에 덮어쓸 수 있습니다.
    echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc
    echo "> 엔진엑스 Reload"
    sudo service nginx reload    # Nginx reload를 합니다.
}