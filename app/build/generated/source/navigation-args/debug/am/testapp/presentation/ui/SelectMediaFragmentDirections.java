package am.testapp.presentation.ui;

import am.testapp.R;
import androidx.annotation.NonNull;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;

public class SelectMediaFragmentDirections {
  private SelectMediaFragmentDirections() {
  }

  @NonNull
  public static NavDirections actionSelectMediaFragmentToMediaListFragment() {
    return new ActionOnlyNavDirections(R.id.action_selectMediaFragment_to_mediaListFragment);
  }
}
