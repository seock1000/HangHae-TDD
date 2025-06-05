import http from 'k6/http';
import { check } from 'k6';

export const options = {
    stages: [
        { duration: '30s', target: 10 },   // warm-up
        { duration: '30s', target: 20 },
        { duration: '30s', target: 50 },   // 점진 부하 증가
        { duration: '30s', target: 100 },
        { duration: '30s', target: 200 },
        { duration: '30s', target: 300 },
        { duration: '30s', target: 300 },  // 유지
        { duration: '30s', target: 0 },    // 종료
    ],
    thresholds: {
        checks: ['rate>0.95'],
        http_reqs: ['rate>100'],
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
