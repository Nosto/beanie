#!/bin/bash

#
#  Copyright (c) 2021 Nosto Solutions Ltd All Rights Reserved.
#
#  This software is the confidential and proprietary information of
#  Nosto Solutions Ltd ("Confidential Information"). You shall not
#  disclose such Confidential Information and shall use it only in
#  accordance with the terms of the agreement you entered into with
#  Nosto Solutions Ltd.
#

docker run --env GITHUB_WORKSPACE=/app --volume="$(pwd)"/.:/app docker.pkg.github.com/mridang/action-idea/idealize:2020.3.3 /app /app/.idea/inspectionProfiles/CI.xml /out v2 Inspection noop 1347
