package ua.nure.plug.service.shingle;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.nure.plug.model.Shingle;
import ua.nure.plug.service.HashService;
import ua.nure.plug.service.text.Token;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ShingleServiceImplTest {

    @Mock
    private HashService hashService;

    @InjectMocks
    private ShingleService shingleService = new ShingleServiceImpl();

    @Test
    public void shouldCreateShinglesFromWorkTokens() {
        // given
        when(hashService.md5(anyString())).thenReturn("hash");
        List<Token> tokens = Lists.newArrayList(
                new Token("apple", 0, 5),
                new Token("banana", 6, 12),
                new Token("kiwi", 13, 17),
                new Token("cherry", 18, 24),
                new Token("pear", 25, 29)
        );

        // when
        List<Shingle> shingles = shingleService.createShingles(tokens);

        // then
        Shingle shingle1 = shingles.get(0);
        assertEquals("applebananakiwi", shingle1.getShingle());
        assertEquals(0, shingle1.getStart());
        assertEquals(17, shingle1.getEnd());

        Shingle shingle2 = shingles.get(1);
        assertEquals("bananakiwicherry", shingle2.getShingle());
        assertEquals(6, shingle2.getStart());
        assertEquals(24, shingle2.getEnd());

        Shingle shingle3 = shingles.get(2);
        assertEquals("kiwicherrypear", shingle3.getShingle());
        assertEquals(13, shingle3.getStart());
        assertEquals(29, shingle3.getEnd());
    }

}