package fun.hercules.user.enterprise.service;


import fun.hercules.user.enterprise.domain.Enterprise;
import fun.hercules.user.enterprise.domain.EnterpriseBase.ValidationStatus;
import fun.hercules.user.enterprise.domain.EnterpriseQualificationHistory;
import fun.hercules.user.enterprise.repository.EnterpriseQualificationHistoryRepository;
import fun.hercules.user.enterprise.domain.Enterprise;
import fun.hercules.user.enterprise.domain.EnterpriseQualificationHistory;
import fun.hercules.user.enterprise.repository.EnterpriseQualificationHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class EnterpriseQualificationHistoryService {

    @Autowired
    private EnterpriseQualificationHistoryRepository qualificationHistoryRepository;

    /**
     * 为企业保存审核历史记录（用于创建企业、更新审核信息时使用）
     *
     * @param enterprise 企业信息
     * @return 企业审核历史记录
     */
    public EnterpriseQualificationHistory saveHistoryFor(Enterprise enterprise) {
        EnterpriseQualificationHistory qualificationHistory = EnterpriseQualificationHistory.builder()
                .enterprise(enterprise).enterpriseId(enterprise.getId()).build();
        return qualificationHistoryRepository.save(qualificationHistory);
    }

    /**
     * 更新企业审核历史信息状态，及评论
     *
     * @param enterpriseId 需要更新审核信息的企业Id
     * @param status       认证状态
     * @param comment      评论
     */
    public void updateQualificationHistory(String enterpriseId, ValidationStatus status, String comment) {
        EnterpriseQualificationHistory latestQualificationRecord = qualificationHistoryRepository
                .findTopByEnterpriseIdOrderByCreatedAtDesc(enterpriseId).orElseThrow(() -> new RuntimeException(""));
        latestQualificationRecord.setComment(comment);
        latestQualificationRecord.setValidationStatus(status);
    }

    public List<EnterpriseQualificationHistory> getEnterpriseQualificationHistories(String enterpriseId) {
        return qualificationHistoryRepository.findByEnterpriseIdOrderByCreatedAtDesc(enterpriseId);
    }
}
