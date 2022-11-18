package am.testapp.presentation.ui.media_list_fragment;

import am.testapp.R;
import androidx.annotation.NonNull;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;

public class MediaListFragmentDirections {
  private MediaListFragmentDirections() {
  }

  @NonNull
  public static NavDirections actionMediaListFragmentToSuccessFragment() {
    return new ActionOnlyNavDirections(R.id.action_mediaListFragment_to_successFragment);
  }
}
