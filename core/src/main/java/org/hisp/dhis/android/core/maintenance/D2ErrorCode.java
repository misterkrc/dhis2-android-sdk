/*
 * Copyright (c) 2004-2019, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.hisp.dhis.android.core.maintenance;

public enum D2ErrorCode {
    ALREADY_AUTHENTICATED,
    ALREADY_EXECUTED,
    API_UNSUCCESSFUL_RESPONSE,
    API_INVALID_QUERY,
    API_RESPONSE_PROCESS_ERROR,
    BAD_CREDENTIALS,
    CANT_CREATE_EXISTING_OBJECT,
    CANT_DELETE_NON_EXISTING_OBJECT,
    LOGIN_USERNAME_NULL,
    LOGIN_PASSWORD_NULL,
    NO_AUTHENTICATED_USER,
    NO_AUTHENTICATED_USER_OFFLINE,
    DIFFERENT_AUTHENTICATED_USER_OFFLINE,
    DIFFERENT_SERVER_OFFLINE,
    INVALID_DHIS_VERSION,
    NO_RESERVED_VALUES,
    OBJECT_CANT_BE_UPDATED,
    OWNERSHIP_ACCESS_DENIED,
    SEARCH_GRID_PARSE,
    SOCKET_TIMEOUT,
    TOO_MANY_ORG_UNITS,
    TOO_MANY_PERIODS,
    UNEXPECTED,
    UNKNOWN_HOST,
    URL_NOT_FOUND,
    USER_ACCOUNT_DISABLED,
    USER_ACCOUNT_LOCKED
}