import React from 'react'
import SearchHeader from './SearchHeader.jsx';
import "./UserProfile.css"

const UserProfile = (props) => {
  const user = props.user;
  return (
    <div className='user-profile-banner'> 
        <SearchHeader user= {user} movie={props.movie}/>
    </div>
  )
}

export default UserProfile
