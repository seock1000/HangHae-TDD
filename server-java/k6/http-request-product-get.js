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
            vus: 10,
            iterations: 100,
            maxDuration: "30s",
        },
    },
};

export default function () {
    const index = (__VU - 1) * 10 + __ITER;
    const page = Math.floor(index / 10) + 1;
    let res = http.get(`http://localhost:8080/api/v1/products?page=${page}`);

    const success = check(res, {
        "status is 200": (r) => r.status === 200,
    });

    if (success) {
        successCount.add(1);
    } else {
        failCount.add(1);
    }
}