You need to set the value of upload_max_filesize and post_max_size in your php.ini :

; Maximum allowed size for uploaded files.
upload_max_filesize = 40M

; Must be greater than or equal to upload_max_filesize
post_max_size = 40M

; Maximum allowed size for uploaded files.
; https://php.net/upload-max-filesize
upload_max_filesize=10G

post_max_size=10G