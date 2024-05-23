import React, { useEffect, useState, useCallback } from "react";
import Dropdown from "react-bootstrap/Dropdown";
import DropdownButton from "react-bootstrap/DropdownButton";
import "./SearchHeader.css";
import Movie from "../Movie/Movie";
import UserHeader from "./UserHeader";
import { toast } from "react-toastify";
import "@fortawesome/fontawesome-svg-core/styles.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faAngleUp,
  faAngleDown,
  faPowerOff,
} from "@fortawesome/free-solid-svg-icons";

const SearchHeader = (props) => {
  const [title, setTitle] = useState("SELECT GENRE");
  const [movie, setMovie] = useState(props.movie);
  const [genres, setGenres] = useState([]);
  const storedUser = localStorage.getItem("currentUser");
  const userArray = JSON.parse(storedUser);
  const [order, setOrder] = useState("asc");
  const [minRating, setMinRating] = useState(0);
  const [maxRating, setMaxRating] = useState(0);
  const [error, setError] = useState("");
  const [isSearchVisible, setSearchVisible] = useState(false);
  const [isUp, setIsUp] = useState(false);

  useEffect(() => {
    const url = `http://localhost:8080/genre/get`;
    const baseurl = `http://localhost:8080/movie/rating/7/9/desc`;

    const requestOptions = {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    };

    const response = fetch(url, requestOptions)
      .then((response) => response.json())
      .then((data) => {
        const values = Object.values(data);
        setGenres(values);
      });

    fetch(baseurl, requestOptions)
      .then((response) => {
        if (!response.ok) {
          throw new Error("Network response was not ok");
        }
        return response.json();
      })
      .then((data) => {
        setMovie([]);
        setMovie(data);
      })
      .catch((error) => {
        console.error("There was a problem with the fetch operation:", error);
      });
  }, [props]);

  const handleItemClick = (index, name) => {
    setTitle(name);
  };

  const getMovieByGenre = useCallback(() => {
    const url = `http://localhost:8080/movie/genre/${title}`;
    const requestOptions = {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    };

    fetch(url, requestOptions)
      .then((response) => {
        if (!response.ok) {
          throw new Error("Network response was not ok");
        }
        return response.json();
      })
      .then((data) => {
        setMovie([]);
        setMovie(data);
      })
      .catch((error) => {
        console.error("There was a problem with the fetch operation:", error);
      });
  });

  const handleInputChange = (e, setInput) => {
    const value = e.target.value;
    if (value === "" || (value >= 0 && value <= 10)) {
      setInput(value);
    }
  };

  const handleAddGenre = useCallback(() => {
    const url = `http://localhost:8080/user/edit`;
    const key = "Bearer " + localStorage.getItem(userArray[0]);
    const genre = {
      3: {
        genreId: "2",
        name: title,
      },
    };
    const requestOptions = {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: key,
      },
      body: JSON.stringify({
        username: userArray[1],
        preferredGenres: genre,
      }),
    };

    fetch(url, requestOptions)
      .then((response) => {
        if (!response.ok) {
          toast.warning("Genre exists already");
        } else {
          
          return response.text();
        }
      })
      .then((data) => {
        toast.success(data);
      });
  }, [userArray, title]);

  const handleDeleteGenre = useCallback(() => {
    const url = `http://localhost:8080/user/remove`;
    const key = "Bearer " + localStorage.getItem(userArray[0]);
    const genre = {
      3: {
        genreId: "2",
        name: title,
      },
    };
    const requestOptions = {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
        Authorization: key,
      },
      body: JSON.stringify({
        username: userArray[1],
        preferredGenres: genre,
      }),
    };

    fetch(url, requestOptions)
      .then((response) => {
        if (!response.ok) {
          toast.warning("Genre Not Exists");
        } else {
          
          return response.text();
        }
      })
      .then((data) => {
        toast.success(data);
      });
  }, [userArray, title]);

  const handlegetPreferredMovies = useCallback(() => {
    const url = `http://localhost:8080/movie/preferences/${userArray[0]}`;

    const requestOptions = {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    };

    fetch(url, requestOptions)
      .then((response) => {
        if (!response.ok) {
          toast.error("Network Error");
        }
        return response.json();
      })
      .then((data) => {
        setMovie([]);
        setMovie(data);
      });
  }, []);

  const handleSearchMovieByRatings = useCallback(() => {
    const url = `http://localhost:8080/movie/rating/${minRating}/${maxRating}/${order}`;
    const requestOptions = {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    };

    fetch(url, requestOptions)
      .then((response) => {
        if (!response.ok) {
          toast.error("Network response was not ok");
        }
        return response.json();
      })
      .then((data) => {
        setMovie([]);
        setMovie(data);
      })
      .catch((error) => {
        toast.error("Failed to fetch data");
      });
  });

  const handleLogout = () => {
    const url = `http://localhost:8080/user/logout`;
    const key = "Bearer " + localStorage.getItem(userArray[0]);
    
    const requestOptions = {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: key,
      },
      body: JSON.stringify({
        username: userArray[1]
      }),
    };

    fetch(url, requestOptions)
      .then((response) => {
        if (!response.ok) {
          toast.warning("Something wrong happened");
        } else {
          toast.success("Logged out successfully");
          localStorage.removeItem(userArray[0]);
    localStorage.removeItem("currentUser");
    window.location.href = "/";
          return response.text();
        }
      })
    
  };
  const toggleSearch = () => {
    setSearchVisible(!isSearchVisible);
    setIsUp(!isUp);
  };

  return (
    <>
      <div className="search-header">
        <div className="button-header">
          <button className="btn btn-primary arrow" onClick={toggleSearch}>
            <FontAwesomeIcon
              icon={isUp ? faAngleUp : faAngleDown}
              size="lg"
            />
          </button>
          <button className="btn btn-primary arrow" onClick={handleLogout}>
            <FontAwesomeIcon icon={faPowerOff} size="lg" />
          </button>
        </div>
        {isSearchVisible && (
          <div className="search-banner">
            <div className="top-header">
              <div className="search-genre">
                <div className="genre">
                  <DropdownButton id="dropdown-item-button" title={title}>
                    {genres &&
                      genres.map((genre, index) => (
                        <Dropdown.Item
                          as="button"
                          value={genre.name}
                          onClick={() => handleItemClick(index, genre.name)}
                          key={index}
                        >
                          {genre.name}
                        </Dropdown.Item>
                      ))}
                  </DropdownButton>
                  <button
                    type="button"
                    className="btn btn-outline-primary"
                    onClick={getMovieByGenre}
                  >
                    Search
                  </button>
                  <div className="search-preferences">
                    <button
                      type="button"
                      className="btn btn-outline-primary"
                      onClick={handlegetPreferredMovies}
                    >
                      Recommend Movies
                    </button>
                  </div>
                </div>
                <div className="preferences">
                  <button
                    type="button"
                    className="btn btn-outline-primary"
                    onClick={handleAddGenre}
                  >
                    Add Genre
                  </button>
                  <button
                    type="button"
                    className="btn btn-outline-primary"
                    onClick={handleDeleteGenre}
                  >
                    Delete Genre
                  </button>
                </div>
              </div>
              <div className="container mt-5 rating-container">
                <div className="form-group">
                  <label htmlFor="minRating">Min Rating</label>
                  <input
                    type="number"
                    className="form-control"
                    id="minRating"
                    value={minRating}
                    placeholder="Enter a rating between 0 and 10"
                    step="0.1"
                    min="0"
                    max="10"
                    onChange={(e) => handleInputChange(e, setMinRating)}
                  />
                </div>
                <div className="form-group">
                  <label htmlFor="maxRating">Max Rating</label>
                  <input
                    type="number"
                    className="form-control"
                    id="maxRating"
                    value={maxRating}
                    placeholder="Enter a rating between 0 and 10"
                    step="0.1"
                    min="0"
                    max="10"
                    onChange={(e) => handleInputChange(e, setMaxRating)}
                  />
                </div>
                <div className="form-group">
                  <label htmlFor="order">Order</label>
                  <select
                    className="form-control"
                    id="order"
                    value={order}
                    onChange={(e) => setOrder(e.target.value)}
                  >
                    <option value="asc">Ascending</option>
                    <option value="desc">Descending</option>
                  </select>
                </div>
                <button
                  className="btn btn-primary"
                  onClick={handleSearchMovieByRatings}
                >
                  Search Movies
                </button>
                {error && <p className="text-danger mt-3">{error}</p>}
              </div>
            </div>
          </div>
        )}
      </div>
      <UserHeader />
      <Movie movie={movie} />
    </>
  );
};

export default SearchHeader;
