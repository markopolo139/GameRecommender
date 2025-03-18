package ms.gamerecommender.business.excpetions;

public class InvalidPredictModelException extends BusinessException {
    public InvalidPredictModelException(String message) {
        super(message);
    }
}
