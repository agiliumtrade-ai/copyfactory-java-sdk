2.1.1
  - Fixed dependency vulnerabilities

2.1.0
  - breaking change: added option to filter transactions by account id in history API and altered API parameter list as a result
  - handle TooManyRequestsError in HTTP client
  - added settings to disable SL and/or TP copying
  - added settings to specify max and min trade volume
  - added settings to configure trade size scaling

1.1.1
  - Fixed CopyFactory constructor without additional options

1.1.0
  - Added API to retrieve account / strategy / portfolio strategy by id

1.0.0
  - CopyFactory SDK is now a separate repository/module, migrated from metaapi.cloud javascript SDK