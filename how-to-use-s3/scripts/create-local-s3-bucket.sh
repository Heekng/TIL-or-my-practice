#!/bin/bash

echo $AWS_DEFAULT_REGION

awslocal s3 mb s3://test-bucket

awslocal s3api list-buckets