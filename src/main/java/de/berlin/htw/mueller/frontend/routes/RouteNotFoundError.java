package de.berlin.htw.mueller.frontend.routes;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;

/**
 * Component that handles non-existing routes.
 */
@Tag(Tag.DIV)
public class RouteNotFoundError extends Component
        implements HasErrorParameter<NotFoundException> {

    private final Logger logger = LoggerFactory.getLogger(RouteNotFoundError.class);

    /**
     * Forwards a user to the StartView if a given route is nonexistent.
     * @param event is an event used to forward the user to another route.
     * @param parameter are error parameters.
     * @return
     */
    @Override
    public int setErrorParameter(BeforeEnterEvent event,
                                 ErrorParameter<NotFoundException> parameter) {
        logger.error("Could not navigate to {}", event.getLocation().getPath());
        event.forwardTo(StartView.class);
        return HttpServletResponse.SC_NOT_FOUND;
    }
}