function changeMenuStyle(obj, new_style) { 
  obj.className = new_style; 
}

function showCursor(){
	document.body.style.cursor='hand'
}

function hideCursor(){
	document.body.style.cursor='default'
}

function confirmDelete(){
  if (confirm('Are you sure you want to delete?')){
    return true;
    }else{
    return false;
  }
}


function validate_form()
{
    valid = true;
    if ( document.form[0].label.value.length == 0 )
    {
        alert ( "Please specify a label." );
        return false;
    }
    if ( document.form[0].rootConceptCode.length == 0 )
    {
        alert ( "Please specify a root concept code." );
        return false;
    }    
    return valid;
}