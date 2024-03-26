#!/bin/bash

awslocal sns create-topic --name audio-records-sns --region us-east-1

awslocal sqs create-queue --queue-name audio-transcription-sqs --region us-east-1
awslocal sns subscribe --topic-arn "arn:aws:sns:us-east-1:000000000000:audio-records-sns" \
	--protocol sqs \
	--attributes RawMessageDelivery=true \
	--notification-endpoint "arn:aws:sqs:us-east-1:000000000000:audio-transcription-sqs"
