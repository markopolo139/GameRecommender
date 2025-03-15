package ms.gamerecommender.business.service.ai.ncf;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.nn.core.Embedding;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.io.IOException;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserGameEmbedding extends Embedding<Integer> {

    protected UserGameEmbedding(int numEmbeddings, int embeddingSize) {
        super(new UserGameEmbeddingBuilder().optNumEmbeddings(numEmbeddings).setEmbeddingSize(embeddingSize));
    }

    @Override
    public byte[] encode(Integer input) throws IOException {
        return new byte[]{(byte) (input >> 24), (byte) (input >> 16), (byte) (input >> 8), (byte) input.intValue()};
    }

    @Override
    public Integer decode(byte[] byteArray) throws IOException {
        return (byteArray[0] & 0xFF) << 24 | (byteArray[1] & 0xFF) << 16 | (byteArray[2] & 0xFF) << 8 | (byteArray[3] & 0xFF);
    }

    @Override
    public long embed(Integer item) {
        return item;
    }

    @Override
    public Optional<Integer> unembed(long index) {
        return Optional.of((int) index);
    }

    @Override
    public boolean hasItem(Integer item) {
        return item >= 0 && item < numEmbeddings;
    }

    @Override
    public NDArray embed(NDManager manager, Integer[] items) {
        long[] indices = new long[items.length];
        for (int i = 0; i < items.length; i++) {
            indices[i] = items[i];
        }
        NDArray indicesArray = manager.create(indices);
        return embedding.getArray().get(indicesArray);
    }

    private static class UserGameEmbeddingBuilder extends BaseBuilder<Integer, UserGameEmbeddingBuilder> {

        @Override
        protected UserGameEmbeddingBuilder setType(Class<Integer> embeddingType) {
            return this;
        }

        @Override
        protected UserGameEmbeddingBuilder self() {
            return this;
        }
    }
}
