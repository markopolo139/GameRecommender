package ms.gamerecommender.web.exception;

import ms.gamerecommender.app.exceptions.AppException;
import ms.gamerecommender.app.exceptions.IncorrectPasswordException;
import ms.gamerecommender.app.exceptions.UsernameUsedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler({UsernameUsedException.class, IncorrectPasswordException.class, AppException.class})
    @ResponseBody
    public String handleCustomExceptions(Exception ex) {
        return """
            <div id="error-modal" class="modal fade" tabindex="-1">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content">
                        <div class="modal-header bg-danger text-white">
                            <h5 class="modal-title">Error</h5>
                            <button type="button" class="btn-close btn-close-white" 
                                    data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <p>%s</p>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" 
                                    data-bs-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>
            <script>
                document.addEventListener('DOMContentLoaded', function() {
                    var errorModal = new bootstrap.Modal(document.getElementById('error-modal'));
                    errorModal.show();
                });
            </script>
            """.formatted(ex.getMessage());
    }
}
