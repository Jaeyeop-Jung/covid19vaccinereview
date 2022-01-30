#!/usr/bin/env bash

# 쉬고 있는 profile 찾기

function find_idle_profile()
{
    RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}"http://localhost/nginxConfig)

    if [ ${RESPONSE_CODE} -ge 400]
    then
        CURRENT_PROFILE=real2
    else
        CURRENT_PROFILE=$(curl -s http://localhost/nginxConfig)
    fi

    if [ ${CURRENT_PROFILE} == real1 ]
    then
        IDLE_PROFILE=real2
    else
        IDLE_PROFILE=real1
    fi

    echo "${IDLE_PROFILE}"
}

# 쉬고 있는 profile의 port 찾기
function find_idle_profile()
{
    IDLE_PROFILE=$(find_idle_profile)

    if [ ${IDLE_PROFILE} == real1]
    then
        echo "8081"
    else
        echo "8082"
    fi
}