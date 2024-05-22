package org.issk.service;

import org.issk.dao.UserDao;


import org.issk.dto.Genre;

import org.issk.dto.Movie;
import org.issk.dto.Session;
import org.issk.dto.User;
import org.issk.exceptions.InvalidInputException;
import org.issk.exceptions.InvalidSessionException;
import org.issk.exceptions.SessionNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.binary.Hex;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

import java.util.Map;



@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public int createUser(User user) {
        if ((!user.getUsername().isEmpty()) && (user.getPassword().length() >= 8)) {
            try {
                return userDao.createUser(user) ? 204 : 500;
            } catch (NoSuchAlgorithmException e) {
                return 500;
            }
        } else {
            return 400;
        }
    }

    @Override
    public Session login(User user) {
        //Check to see if the username and password match one in the database
        boolean validUser;
        try {
            validUser = userDao.authenticateUser(user);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }

        if (validUser) {
            //Get single instance of current time
            LocalDateTime now = LocalDateTime.now();

            //Get user from database
            user = userDao.getUserByUsername(user.getUsername());

            //Update user preferences from database
            user = userDao.getUserPreferences(user);

            //Create sessionHash of username and current time
            //Turns the byte array from enHash into a string
            String sessionId;
            try {
                sessionId = Hex.encodeHexString(userDao.enHash(user.getUsername() + now));
            } catch (NoSuchAlgorithmException e) {
                return null;
            }

            //Create new session
            Session session = new Session();
            session.setUser(user);
            session.setStartTime(now);
            session.setPeriodHours(1);
            session.setSessionId(sessionId);

            //Store the session in the database
            try {
                userDao.storeSession(session);
            } catch (NoSuchAlgorithmException e) {
                return null;
            }

            //Return it to the caller
            return session;
        }
        return null;
    }

    @Override
    public ResponseEntity<String> logout(HttpServletRequest request, User user) {
        try {
            Session session = checkValidUser(request, user);
            if (userDao.removeSession(session)) {
                return ResponseEntity.ok("User logged out successfully");
            } else {
                return ResponseEntity.badRequest().body("Failed to log out");
            }
        } catch (InvalidSessionException | SessionNotFoundException e) {
            return ResponseEntity.badRequest().body("Invalid session or session not found");
        } catch (NoSuchAlgorithmException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @Override
    public ResponseEntity<String> editPreferences(HttpServletRequest request, User user) {
        try {
            // Validate the user session
            Session session = checkValidUser(request, user);

            // Set the user ID from the session to the user object
            user.setUserId(session.getUser().getUserId());

            // Validate the preferred genre input
            boolean isValidInput = checkValidInputsForGenre(user);
            if (isValidInput) {
                //  To check whether edit user preference is success
                boolean isEdited = userDao.editPreferences(user);
                if (isEdited) {
                    return ResponseEntity.ok("Preferences edited successfully");
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to edit preferences: Preference already exists");
                }
            } else {
                return ResponseEntity.badRequest().body("Invalid preferred genre input");
            }

        } catch ( InvalidSessionException | SessionNotFoundException e) {
            return ResponseEntity.badRequest().body("Invalid session or session not found");
        }
        catch (InvalidInputException | DataAccessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (NoSuchAlgorithmException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }


    @Override
    public ResponseEntity<String> removePreferences(HttpServletRequest request, User user) {
        try {
            // Validate the user session
            Session session = checkValidUser(request, user);

            // Set the user ID from the session to the user object
            user.setUserId(session.getUser().getUserId());

            // Validate the preferred genre input
            boolean isValidInput = checkValidInputsForGenre(user);
            if (isValidInput) {
                // Attempt to edit user preferences in the database
                boolean isRemoved = userDao.removePreferences(user);
                if (isRemoved) {
                    return ResponseEntity.ok("Genres removed successfully");
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to remove genres: No Genre exists");
                }
            } else {
                return ResponseEntity.badRequest().body("Invalid preferred genre input");
            }

        } catch (InvalidInputException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (InvalidSessionException | SessionNotFoundException e) {
            return ResponseEntity.badRequest().body("Invalid session or session not found");
        } catch (NoSuchAlgorithmException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @Override
    public ResponseEntity<String> deleteUser(HttpServletRequest request, User user) {
        try {
            Session session = checkValidUser(request, user);
            user.setUserId(session.getUser().getUserId());
            if (userDao.deleteUser(user)) {
                return ResponseEntity.ok("User removed successfully");
            } else {
                return ResponseEntity.badRequest().body("Failed to remove user");
            }
        } catch (InvalidSessionException | SessionNotFoundException e) {
            return ResponseEntity.badRequest().body("Invalid session or session not found");
        } catch (NoSuchAlgorithmException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @Override
    public ResponseEntity<String> addFavouriteMovies(HttpServletRequest request, User user) {

        try {
            // Validate the user session
            Session session = checkValidUser(request, user);

            // Set the user ID from the session to the user object
            user.setUserId(session.getUser().getUserId());

            boolean isValidInput = checkValidInputsForMovie(user);
            if (isValidInput) {

                boolean isAdded = userDao.addFavouriteMovies(user);
                if (isAdded) {
                    return ResponseEntity.ok("Movies added successfully");
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add movies: Movie already exists");
                }
            } else {
                return ResponseEntity.badRequest().body("Invalid movie input");
            }

        } catch (InvalidInputException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (InvalidSessionException | SessionNotFoundException e) {
            return ResponseEntity.badRequest().body("Invalid session or session not found");
        } catch (NoSuchAlgorithmException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    public Session checkValidUser(HttpServletRequest request, User user) throws NoSuchAlgorithmException, InvalidSessionException, SessionNotFoundException {

//        Get sessionID from the header
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new InvalidSessionException("Invalid session ID");
        }

        String sessionId = authorizationHeader.substring("Bearer ".length());
        Session session = userDao.getSessionById(sessionId);

        if (session == null) {
            throw new SessionNotFoundException("Session not found");
        }

        if (!session.getUser().getUsername().equals(user.getUsername())) {
            throw new InvalidSessionException("Session does not match the username");
        }

        if (!userDao.checkSessionValid(session)) {
            throw new InvalidSessionException("Session is not valid");
        }

        return session;
    }

    public boolean checkValidInputsForGenre(User user) throws InvalidInputException {

        if (user.getUsername().equals("")) {
            throw new InvalidInputException("Enter valid username");
        } else if (user.getPreferredGenres().isEmpty()) {
            throw new InvalidInputException("Enter valid genre names");
        } else {

            for (Map.Entry<Integer, Genre> entry : user.getPreferredGenres().entrySet()) {
                Integer key = entry.getKey();
                Genre genre = entry.getValue();

                if (key == null || genre == null || genre.getGenreId() == 0 || genre.getName().equals("")) {
                    throw new InvalidInputException("Enter valid genre names");
                }
            }

        }
        return true;
    }

    public boolean checkValidInputsForMovie(User user) throws InvalidInputException {

        if (user.getUsername().equals("")) {
            throw new InvalidInputException("Enter valid username");
        } else if (user.getFavouriteMovies().isEmpty()) {
            throw new InvalidInputException("Enter valid movie names");
        } else {
            for (Map.Entry<Integer, Movie> entry : user.getFavouriteMovies().entrySet()) {
                Integer key = entry.getKey();
                Movie movie = entry.getValue();

                if (key == null || movie == null || movie.getId() == 0 || movie.getTitle().equals("")) {
                    throw new InvalidInputException("Enter valid movie names");
                }
            }

        }
        return true;
    }

}
