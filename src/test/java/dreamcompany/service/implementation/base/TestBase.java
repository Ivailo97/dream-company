package dreamcompany.service.implementation.base;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//integration tests base

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestBase {

    @Before
    public void setupTest() {
        MockitoAnnotations.initMocks(this);
        this.before();
    }

    protected void before() {
    }
}
