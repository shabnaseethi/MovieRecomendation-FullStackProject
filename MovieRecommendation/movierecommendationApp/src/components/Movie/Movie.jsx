import React, { useState, useEffect, useCallback } from "react";
import { toast } from "react-toastify";

const Movie = (props) => {
  const [popularMovies, setPopularMovies] = useState(props.movie || []);
  const storedUser = localStorage.getItem("currentUser");
  const userArray = JSON.parse(storedUser);

  useEffect(() => {
    if (props.movie) {
      setPopularMovies(props.movie);
    }
  }, [props.movie]);

  const handleAddFavourites = useCallback(
    (id) => {
      const url = `http://localhost:8080/user/add-movie`;
      const key = "Bearer " + localStorage.getItem(userArray[0]);
      const movie = {
        27205: {
          id: id,
          title: "A Walk to Remember",
        },
      };
      const requestOptions = {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: key,
        },
        body: JSON.stringify({
          username: userArray[1],
          favouriteMovies: movie,
        }),
      };

      fetch(url, requestOptions)
        .then((response) => {
          if (!response.ok) {
            toast.warning("Movie already exists!!!");
          } else {
            return response.text();
          }
        })
        .then((data) => {
          toast.success(data);
        });
    },
    [props]
  );

  return (
    <>
      <div className="container">
        <div className="row row-cols-1 row-cols-md-3 g-4">
          {popularMovies !== null && popularMovies.length > 0 ? (
            popularMovies.map((movie, key) => (
              <div className="col" key={key}>
                <div className="card h-100">
                  <img
                    className="card-img-top"
                    src={`https://image.tmdb.org/t/p/original${
                      movie && movie.poster_path
                    }`}
                    alt="Card image cap"
                  />
                  <div className="card-body">
                    <h5 className="card-title">{movie ? movie.title : ""}</h5>
                    <button
                      type="button"
                      className="btn btn-success"
                      onClick={() => handleAddFavourites(movie.id)}
                    >
                      Add to Favourites
                    </button>
                  </div>
                </div>
              </div>
            ))
          ) : (
            <div></div>
          )}
        </div>
      </div>
    </>
  );
};

export default Movie;
