import http from "k6/http";
import { check } from "k6";
import { Counter } from "k6/metrics";

// 커스텀 메트릭 정의
const successCount = new Counter("successful_requests");
const failCount = new Counter("failed_requests");

export const options = {
    discardResponseBodies: true,
    scenarios: {
        contacts: {
            executor: "per-vu-iterations",
            vus: 1,
            iterations: 100,
            maxDuration: "5s",
        },
    },
};

export default function () {
    const userId = __ITER + 1; // 1~100
    const url = 'http://localhost:8080/api/v1/coupons/issue';
    const payload = JSON.stringify({
        userId : 1,
        couponId : 3
    })
    const params = {
        headers : {
            'Content-Type' : 'application/json',
        }
    }
    let res = http.post(url, payload, params);

    const success = check(res, {
        "status is 200": (r) => r.status === 200,
    });

    if (success) {
        successCount.add(1);
    } else {
        failCount.add(1);
    }
}