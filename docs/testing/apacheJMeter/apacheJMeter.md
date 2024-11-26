# Performance Test Report

## Contents

- [Introduction](#introduction)
- [Test Methodology](#test-methodology)
- [Results](#results)
- [Anomaly](#anomaly)
- [Conclusion](#conclusion)

---

**Project:** Student Information System

**Scope:** Landing Page (/profile)

**Protocol:** HTTP Request (GET Operation)

**Tool Used:** Apache JMeter

## Introduction
The purpose of this performance test was to evaluate the system's ability to handle a large number of concurrent users without significant performance degradation.

---

## Test Methodology
The test was conducted using the Apache JMeter tool, utilizing the HTTP protocol with GET requests.

**Load Test Parameters:**
- **Number of Threads (Users):** 1000
- **Ramp-Up Period:** 3 seconds
- **Loop Count:** 10

![apache JMeter Thread Group Settings](../../../images/test_tools/apacheJMeter/ThreadGroupSettings.png)
![apache JMeter HTTP Request Settings](../../../images/test_tools/apacheJMeter/HTTPRequestSettings.png)
---

## Results
-**Test Duration:** 33 seconds
-**Request status:** 100% success rate
-**Median Latency:** 1253 ms
-**Bytes sent per request:** 244 bytes

![apache JMeter Results Table View](../../../images/test_tools/apacheJMeter/ResultsTableView.png)

---

## Anomaly
During the first run of the test, the initial 500 out of 10,000 requests failed randomly.

---

## Conclusion
The system successfully passes the load test and can be considered ready for production deployment. However, as the number of users increases over time, it is advisable to upgrade the hardware to enhance performance.
