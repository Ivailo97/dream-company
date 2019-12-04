package dreamcompany.util;

import dreamcompany.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@AllArgsConstructor
public class ScheduledTask {

    private final TeamRepository teamRepository;

    private final AtomicInteger invocationsCount;

    @Scheduled(fixedRate = 600000)
    public void payTaxesEveryTenMinutes() {
        BigDecimal tax = BigDecimal.valueOf(1);
        teamRepository.findAllByProfitAfter(tax).forEach(t -> {
            t.setProfit(t.getProfit().subtract(tax));
            teamRepository.save(t);
        });

        invocationsCount.incrementAndGet();
    }

    public AtomicInteger getInvocationsCount() {
        return invocationsCount;
    }
}
