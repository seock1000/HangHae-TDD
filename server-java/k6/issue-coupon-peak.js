import http from 'k6/http';
import { check } from 'k6';

export const options = {
    scenarios: {
        peak_test: {
            executor: 'constant-arrival-rate',
            rate: 1000,              // 초당 1000 요청 (목표 RPS)
            timeUnit: '1s',
            duration: '30s',         // 짧고 강한 부하 (총 3만 요청 예상)
            preAllocatedVUs: 200,    // 기본 VU 수 (최소 이만큼 필요)
            maxVUs: 500,             // 필요 시 자동 확장
        },
    },
    thresholds: {
        checks: ['rate>0.95'],
        http_reqs: ['rate>300'],
    },
};

export default function () {
    const userId = `${__ITER}`;
    const payload = JSON.stringify({
        userId: userId,
        couponId: '1',
    });

    const headers = { 'Content-Type': 'application/json' };

    const res = http.post('http://localhost:8080/api/v1/coupons/issue', payload, { headers });

    check(res, {
        '응답 성공': (r) => r.status === 201,
    });
}
