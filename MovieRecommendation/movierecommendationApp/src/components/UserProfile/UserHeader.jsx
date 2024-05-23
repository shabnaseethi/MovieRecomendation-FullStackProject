import React, { useState ,useCallback, useEffect} from "react";
import Movie from "../Movie/Movie";
import { toast } from "react-toastify";

const UserHeader = () => {
 
  const [movie,setMovie] = useState([]);
  const [movieName,setMovieName] = useState("");

  const storedUser = localStorage.getItem('currentUser');
const userArray = JSON.parse(storedUser);


  const handleChange = useCallback((e) => {
    setMovieName(e.target.value);
  }, [movie]);

  const handleGetMovie = useCallback(() => {
    const url = `http://localhost:8080/movie/moviename?name=${encodeURIComponent(movieName)}`;
    const requestOptions = {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
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
        setMovieName("");
        
      })
      .catch((error) => {
        console.error("There was a problem with the fetch operation:", error);
      });
    setMovie({ title: movieName, poster_path: 'path/to/poster' }); 
  }, [movieName]);

  
const handleUserRemoval = () => {
 
  toast.success("User Removed Successfully", {
    autoClose: 1000, 
  });

  
  localStorage.setItem("currentUser", JSON.stringify([]));

  setTimeout(() => {
    window.location.href = '/';
  }, 5000);
};

  const handleDeleteUser = useCallback(() => {
    const url = `http://localhost:8080/user/deleteuser`;
    const key = "Bearer " + localStorage.getItem(userArray[0]);
    
    const requestOptions = {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': key,
      },
      body: JSON.stringify({
        "username": userArray[1]
      }),
    };
  
    fetch(url, requestOptions)
      .then(response => {
        if (!response.ok) {
          toast.warning("User Not Exists");
         
        }
        else{
          
          handleUserRemoval();
        return response.json();
        }
      })
      .then(data => {
       
      })
     
  }, [userArray]);

  
  return (
    <>
      <div className="search-movie-banner">
        <div className="search-movie">
          <div className="input-group mb-3">
            <input
              type="text"
              className="form-control"
              placeholder="Search Movie..."
              aria-describedby="basic-addon2"
              value={movieName}
              onChange={handleChange}
            />
            <div className="input-group-append">
              <button className="btn btn-outline-secondary" type="button" onClick={handleGetMovie}>
               Search
              </button>
            </div>
          </div>
        </div>

        <div className="delete-user">
          <button type="button" className="btn btn-outline-danger" onClick={handleDeleteUser}>
            Delete User
          </button>
        </div>
      </div>
      <Movie movie={movie} />
    </>
  );
};

export default UserHeader;
