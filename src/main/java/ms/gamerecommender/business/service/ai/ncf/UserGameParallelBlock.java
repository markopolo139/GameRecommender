package ms.gamerecommender.business.service.ai.ncf;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.nn.AbstractBlock;
import ai.djl.nn.Block;
import ai.djl.training.ParameterStore;
import ai.djl.util.PairList;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import static ms.gamerecommender.business.service.ai.ncf.NcfModelUtils.*;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserGameParallelBlock extends AbstractBlock {
    Block userEmbedding;
    Block gameEmbedding;

    public UserGameParallelBlock(int numberOfUsers, int numberOfGames, int embeddingSize) {
        this.userEmbedding = new UserGameEmbedding(numberOfUsers, embeddingSize);
        this.gameEmbedding = new UserGameEmbedding(numberOfGames, embeddingSize);
        addChildBlock("userEmbedding", userEmbedding);
        addChildBlock("gameEmbedding", gameEmbedding);
    }

    @Override
    protected NDList forwardInternal(
            ParameterStore parameterStore,
            NDList inputs,
            boolean training,
            PairList<String, Object> params) {
        NDArray userId = inputs.get(0);
        NDArray gameId = inputs.get(1);

        NDList userEmbeddingOutput = userEmbedding.forward(parameterStore, new NDList(userId), training, params);
        NDList gameEmbeddingOutput = gameEmbedding.forward(parameterStore, new NDList(gameId), training, params);

        NDArray concatenated = userEmbeddingOutput.singletonOrThrow()
                .concat(gameEmbeddingOutput.singletonOrThrow(), -1);

        return new NDList(concatenated);
    }

    @Override
    public Shape[] getOutputShapes(Shape[] inputShapes) {
        return new Shape[] { new Shape(inputShapes[0].get(0), EMBEDDING_SIZE * 2L) };
    }

    @Override
    protected void initializeChildBlocks(NDManager manager, DataType dataType, Shape... inputShapes) {
        userEmbedding.initialize(manager, dataType, new Shape());
        gameEmbedding.initialize(manager, dataType, new Shape());
    }
}
