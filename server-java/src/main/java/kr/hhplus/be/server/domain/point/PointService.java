package kr.hhplus.be.server.domain.point;

/**
 * 검증은 도메인에서 완료하였으므로, TC 존재하지 않음 -> 해피케이스만 작성
 */
public class PointService {

    /**
     * charge 와 createChargeHistory는 하나인가 별개인가.
     * 해당 메서드에서 Point.addHistories() 와 같은 동작은 불필요한 History 조회 유발
     * 애초에 History를 조회하면 paging 처리가 들어가지 않나?? -> 그렇다면 이력 조회에도 History를 안쓰는데 참조를 할 필요가 있나?
     */
    public Point charge(Point point, int amount) {
        point.charge(amount);
        return point;
    }

    public Point use(Point point, int amount) {
        point.use(amount);
        return point;
    }

    public PointHistory createChargeHistory(Point point, int amount) {
        return PointHistory.createChargeHistory(point.getId(), amount, point.getBalance());
    }

    public PointHistory useHistory(Point point, int amount) {
        return PointHistory.createUseHistory(point.getId(), amount, point.getBalance());
    }

}
